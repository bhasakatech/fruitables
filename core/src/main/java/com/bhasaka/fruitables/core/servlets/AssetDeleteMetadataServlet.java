package com.bhasaka.fruitables.core.servlets;

import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/assetDeleteMetadata",
                "sling.servlet.methods=POST"}
)
public class AssetDeleteMetadataServlet
        extends SlingAllMethodsServlet {

    @Reference
    private JobManager jobManager;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        log.info("===== SERVLET TRIGGERED =====");
        String[] paths =request.getParameterValues("paths");
        if (paths != null) {
            for (String path : paths) {
                log.info("Asset Path : {}", path);
                Map<String, Object> map = new HashMap<>();
                map.put("assetPath", path);
                jobManager.addJob("fruitables/deleteassetjob",map);
                log.info("Job Created");
            }
        }
        response.getWriter()
                .write("Job Created Successfully");
    }
}