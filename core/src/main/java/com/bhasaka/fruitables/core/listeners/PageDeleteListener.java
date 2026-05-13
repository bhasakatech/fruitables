package com.bhasaka.fruitables.core.listeners;

import com.bhasaka.fruitables.core.service.ServiceUtil;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.*;

@Component(
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=/content/fruitables",
                ResourceChangeListener.CHANGES + "=REMOVED"
        }
)
public class PageDeleteListener implements ResourceChangeListener {

    private static final Logger log =
            LoggerFactory.getLogger(PageDeleteListener.class);

    @Reference
    private ServiceUtil serviceUtil;

    @Override
    public void onChange(List<ResourceChange> changes) {

        log.info("Page deletion event triggered");

        try (ResourceResolver resolver = serviceUtil.getServiceUserMap()) {

            for (ResourceChange change : changes) {

                String deletedPagePath = change.getPath();

                String getUserName = change.getUserId();

                log.info("Deleted page detected : {}", deletedPagePath);

                backupDeletedPageInfo(resolver, deletedPagePath, getUserName);

                deleteRelatedAssets(resolver, deletedPagePath);

                cleanupBrokenReferences(resolver, deletedPagePath);
            }

        } catch (Exception e) {
            log.error("Error while processing page deletion event", e);
        }
    }

    private void backupDeletedPageInfo(ResourceResolver resolver,
                                       String deletedPagePath, String getUserName) {

        try {

            Resource varResource =
                    resolver.getResource("/var/deleted-pages");

            if (varResource == null) {

                log.warn("/var/deleted-pages folder not found");

                return;
            }

            String nodeName =
                    deletedPagePath.substring(
                            deletedPagePath.lastIndexOf("/") + 1);

            Map<String, Object> props = new HashMap<>();
            props.put("jcr:primaryType", "nt:unstructured");

            Resource backupResource =
                    resolver.create(
                            varResource,
                            nodeName,
                            props);

            ModifiableValueMap map =
                    backupResource.adaptTo(ModifiableValueMap.class);

            map.put("pagePath", deletedPagePath);
            map.put("deletedAt", new Date().toString());
            map.put("deletedBy", getUserName);

            resolver.commit();

            log.info("Backup created successfully for : {}",
                    deletedPagePath);

        } catch (Exception e) {

            log.error("Error while backing up deleted page info", e);
        }
    }

    private void deleteRelatedAssets(ResourceResolver resolver,
                                     String deletedPagePath) {

        try {

            String assetPath =
                    deletedPagePath.replace(
                            "/content/fruitables/us/en",
                            "/content/dam/fruitables");

            log.info("Checking related asset path : {}",
                    assetPath);

            Resource assetResource =
                    resolver.getResource(assetPath);

            if (assetResource != null) {

                resolver.delete(assetResource);

                resolver.commit();

                log.info("Deleted related DAM assets : {}",
                        assetPath);

            } else {

                log.warn("No related DAM assets found for : {}",
                        assetPath);
            }

        } catch (Exception e) {

            log.error("Error while deleting DAM assets", e);
        }
    }



    private void cleanupBrokenReferences(ResourceResolver resolver,
                                         String deletedPath) throws RepositoryException {

        log.info("clean up Reference assets Starts : {}", deletedPath);
        try {
            String fallbackPage = "/content/fruitables/us/en/blog-page.html";
            Resource rootResource =
                    resolver.getResource("/content/fruitables/us/en");

            Iterator<Resource> allResources =
                    rootResource.listChildren();

            searchAndUpdateLinks(
                    allResources,
                    deletedPath,
                    fallbackPage
            );

            resolver.commit();

        } catch (Exception e) {

            log.error("Error updating broken references", e);
        }

    }
    private void searchAndUpdateLinks(Iterator<Resource> resources,
                                      String deletedPagePath,
                                      String fallbackPage) {

        while (resources.hasNext()) {

            Resource resource = resources.next();

            ModifiableValueMap map =
                    resource.adaptTo(ModifiableValueMap.class);

            if (map != null) {

                String link = map.get("linkURL", String.class);

                if (deletedPagePath.equals(link)) {

                    map.put("linkURL", fallbackPage);

                    log.info("Updated broken link in {}",
                            resource.getPath());
                }
            }

            searchAndUpdateLinks(
                    resource.listChildren(),
                    deletedPagePath,
                    fallbackPage
            );
        }
    }
}