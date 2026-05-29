package com.bhasaka.fruitables.core.servlets;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/custom/sitemap",
                "sling.servlet.methods=GET"
        }
)
public class SitemapServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws ServletException, IOException {

        try {

            ResourceResolver resolver =
                    request.getResourceResolver();

            Resource resource = resolver.getResource(
                    "/content/dam/fruitables/sitemap.xml/jcr:content/renditions/original/jcr:content"
            );

            if (resource != null) {

                InputStream inputStream =
                        resource.getValueMap()
                                .get("jcr:data", InputStream.class);

                String xml =
                        IOUtils.toString(inputStream, "UTF-8");

                response.setContentType("application/xml");

                response.getWriter().write(xml);

            } else {

                response.getWriter()
                        .write("Sitemap Not Found");
            }

        } catch (Exception e) {

            response.getWriter()
                    .write("Error While Reading Sitemap");
        }
    }
}