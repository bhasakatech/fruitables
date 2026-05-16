package com.bhasaka.fruitables.core.service;

import com.day.cq.dam.api.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=fruitables/deleteassetjob"
        }
)
public class AssetDeleteJobConsumer implements JobConsumer {

    @Reference
    private ServiceUtil serviceUtil;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    public JobResult process(Job job) {

        log.info("========= SLING JOB STARTED =========");

        String assetPath =
                job.getProperty("assetPath", String.class);

        log.info("Received Asset Path : {}", assetPath);

        try (ResourceResolver resolver =
                     serviceUtil.getServiceUserMap()) {

            log.info("Service Resolver Created");

            Resource assetResource =
                    resolver.getResource(assetPath);

            if (assetResource == null) {
                log.error("Asset Resource Not Found");
                return JobResult.FAILED;
            }

            log.info("Asset Resource Found");

            Asset asset =
                    assetResource.adaptTo(Asset.class);

            if (asset == null) {
                log.error("Asset Adaptation Failed");
                return JobResult.FAILED;
            }

            log.info("Asset Adapted Successfully");


              //JSON creation using ObjectMapper

            Map<String, Object> jsonMap =
                    new HashMap<>();

            jsonMap.put(
                    "assetName",
                    asset.getName()
            );

            jsonMap.put(
                    "assetPath",
                    assetPath
            );

            jsonMap.put(
                    "deletedTime",
                    Calendar.getInstance().getTime()
            );

            String jsonData =
                    objectMapper.writeValueAsString(jsonMap);

            log.info("Metadata JSON Created");


             // create /var/deleteAssets

            Resource deleteAssetsFolder =
                    resolver.getResource(
                            "/var/deleteAssets"
                    );

            if (deleteAssetsFolder == null) {

                log.info("/var/deleteAssets Not Present");

                Map<String, Object> folderMap =
                        new HashMap<>();

                folderMap.put(
                        "jcr:primaryType",
                        "sling:Folder"
                );

                deleteAssetsFolder =
                        resolver.create(
                                resolver.getResource("/var"),

                                "deleteAssets",
                                folderMap
                        );

                resolver.commit();

                log.info("/var/deleteAssets Created");
            }

            //  JSON file name

            String fileName =
                    asset.getName()
                            .replace(".", "_")
                            + "_"
                            + System.currentTimeMillis()
                            + ".json";

            log.info("JSON File Name : {}", fileName);


            //create nt:file

            Node fileNode =
                    deleteAssetsFolder
                            .adaptTo(Node.class)
                            .addNode(
                                    fileName,
                                    "nt:file"
                            );

            log.info("nt:file Node Created");


            // create jcr:content

            Node contentNode =
                    fileNode.addNode(
                            "jcr:content",
                            "nt:resource"
                    );

            log.info("jcr:content Node Created");

            /*
             * store json
             */
            contentNode.setProperty(
                    "jcr:data",
                    jsonData
            );

            contentNode.setProperty(
                    "jcr:mimeType",
                    "application/json"
            );

            contentNode.setProperty(
                    "jcr:lastModified",
                    Calendar.getInstance()
            );

            resolver.commit();

            log.info("Metadata JSON Stored Successfully");

            // delete asset
            resolver.delete(assetResource);

            resolver.commit();

            log.info("Asset Deleted Successfully");

            log.info("========= SLING JOB COMPLETED =========");

            return JobResult.OK;

        } catch (Exception e) {

            log.error("Exception Occurred", e);

            return JobResult.FAILED;
        }
    }
}