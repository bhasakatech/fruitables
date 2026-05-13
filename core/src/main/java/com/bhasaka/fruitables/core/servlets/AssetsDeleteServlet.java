package com.bhasaka.fruitables.core.servlets;

import com.bhasaka.fruitables.core.service.CustomDeleteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;

@Slf4j
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/assetsdelete",
                "sling.servlet.methods=POST"
        }
)
public class AssetsDeleteServlet extends SlingAllMethodsServlet {

    @Reference
    private CustomDeleteService customDeleteService;

    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response)
            throws IOException {

        String[] paths = request.getParameterValues("paths");

        if (paths != null) {

            for (String pagePath : paths) {

                log.info("Received delete request for page : {}", pagePath);

                customDeleteService.processDelete(pagePath);
            }
        }

        response.getWriter().write("Delete Process Completed");
    }
}