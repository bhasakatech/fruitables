package com.bhasaka.fruitables.core.schedulers;

import com.bhasaka.fruitables.core.configurations.MetadataSchedulerConfig;
import com.bhasaka.fruitables.core.service.ServiceUtil;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(service = Runnable.class, immediate = true)
@Designate(ocd = MetadataSchedulerConfig.class)
public class XFMetadataScheduler implements Runnable {

    @Reference
    private Scheduler scheduler;

    @Reference
    private ServiceUtil serviceUtil;

    private static final String SCHEDULER_NAME = "XF Metadata Scheduler";

    private String cronExpression;

    private boolean enabled;

    @Activate
    @Modified
    protected void activate(MetadataSchedulerConfig config) {

        cronExpression = config.scheduler_expression();
        enabled = config.scheduler_enabled();

        if (enabled) {

            ScheduleOptions options = scheduler.EXPR(cronExpression);
            options.name(SCHEDULER_NAME);
            options.canRunConcurrently(false);

            scheduler.schedule(this, options);

            log.info("Scheduler Started");
        }
    }

    @Deactivate
    protected void deactivate() {

        scheduler.unschedule(SCHEDULER_NAME);

        log.info("Scheduler Stopped");
    }

    @Override
    public void run() {

        log.info("Scheduler Execution Started");

        ResourceResolver resolver = null;

        try {

            resolver = serviceUtil.getServiceUserMap();

            if (resolver == null) {
                log.error("Resolver is null");
                return;
            }

            /*
             * Experience Fragment Component Path
             */
            String xfComponentPath =
                    "/content/experience-fragments/fruitables/us/en/site/xf-scheduler/master/jcr:content/root/xfscheduler";

            Resource xfResource = resolver.getResource(xfComponentPath);

            if (xfResource == null) {

                log.error("XF Component not found");
                return;
            }

            ValueMap vm = xfResource.getValueMap();

            String[] pagePaths = vm.get("pagePaths", String[].class);

            String[] assetPaths = vm.get("assetPaths", String[].class);

            /*
             * PROCESS PAGES
             */
            if (pagePaths != null) {

                for (String pagePath : pagePaths) {

                    processPage(pagePath, resolver);
                }
            }

            /*
             * PROCESS ASSETS
             */
            if (assetPaths != null) {

                for (String assetPath : assetPaths) {

                    processAsset(assetPath, resolver);
                }
            }

        } catch (Exception e) {

            log.error("Error in Scheduler", e);

        } finally {

            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }

        log.info("Scheduler Execution Completed");
    }

    private void processPage(String pagePath,
                             ResourceResolver resolver) {

        try {

            String jsonNodeName =
                    pagePath.substring(pagePath.lastIndexOf("/") + 1) + ".json";

            /*
             * JSON already exists check
             */
            String pageJsonPath =
                    "/var/PagesJSON/" + jsonNodeName;

            Resource existingJson =
                    resolver.getResource(pageJsonPath);

            if (existingJson != null) {

                log.info("Page Json Already Exists : {}", pagePath);
                return;
            }

            Resource pageResource =
                    resolver.getResource(pagePath);

            if (pageResource == null) {

                log.warn("Page not found : {}", pagePath);
                return;
            }

            Page page = pageResource.adaptTo(Page.class);

            if (page == null) {

                log.warn("Unable to adapt page : {}", pagePath);
                return;
            }

            Map<String, Object> pageMap = new HashMap<>();

            pageMap.put("title", page.getTitle());
            pageMap.put("path", page.getPath());
            pageMap.put("name", page.getName());

            createJsonFile(
                    resolver,
                    "/var/PagesJSON",
                    jsonNodeName,
                    pageMap
            );

            log.info("Page Json Created : {}", pagePath);

        } catch (Exception e) {

            log.error("Error processing page : {}", pagePath, e);
        }
    }

    private void processAsset(String assetPath,
                              ResourceResolver resolver) {

        try {

            String jsonNodeName =
                    assetPath.substring(assetPath.lastIndexOf("/") + 1) + ".json";

            /*
             * JSON already exists check
             */
            String assetJsonPath =
                    "/var/assetJSON/" + jsonNodeName;

            Resource existingJson =
                    resolver.getResource(assetJsonPath);

            if (existingJson != null) {

                log.info("Asset Json Already Exists : {}", assetPath);
                return;
            }

            Resource assetResource =
                    resolver.getResource(assetPath);

            if (assetResource == null) {

                log.warn("Asset not found : {}", assetPath);
                return;
            }

            Asset asset = assetResource.adaptTo(Asset.class);

            if (asset == null) {

                log.warn("Unable to adapt asset : {}", assetPath);
                return;
            }

            Map<String, Object> assetMap = new HashMap<>();

            assetMap.put("name", asset.getName());
            assetMap.put("path", asset.getPath());
            assetMap.put("mimeType", asset.getMimeType());

            createJsonFile(
                    resolver,
                    "/var/assetJSON",
                    jsonNodeName,
                    assetMap
            );

            log.info("Asset Json Created : {}", assetPath);

        } catch (Exception e) {

            log.error("Error processing asset : {}", assetPath, e);
        }
    }

    private void createJsonFile(ResourceResolver resolver,
                                String parentPath,
                                String fileName,
                                Map<String, Object> data)
            throws Exception {

        resolver.refresh();

        Resource parentResource =
                resolver.getResource(parentPath);

        if (parentResource == null) {

            log.error("Parent folder not found : {}", parentPath);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        String jsonString =
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(data);

        /*
         * CREATE nt:file
         */
        Map<String, Object> fileMap = new HashMap<>();

        fileMap.put("jcr:primaryType", "nt:file");

        Resource fileResource =
                resolver.create(parentResource,
                        fileName,
                        fileMap);

        /*
         * CREATE jcr:content
         */
        Map<String, Object> contentMap = new HashMap<>();

        contentMap.put("jcr:primaryType", "nt:resource");

        contentMap.put("jcr:mimeType", "application/json");

        contentMap.put(
                "jcr:data",
                new ByteArrayInputStream(
                        jsonString.getBytes(StandardCharsets.UTF_8)
                )
        );

        resolver.create(
                fileResource,
                "jcr:content",
                contentMap
        );

        resolver.commit();
    }
}