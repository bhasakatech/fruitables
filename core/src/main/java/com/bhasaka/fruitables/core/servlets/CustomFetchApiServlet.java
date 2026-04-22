package com.bhasaka.fruitables.core.servlets;

import com.bhasaka.fruitables.core.service.CustomFetchApiService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/fetch-api",
                "sling.servlet.methods=GET"
        }
)
public class CustomFetchApiServlet extends SlingSafeMethodsServlet {

    @Reference
    private CustomFetchApiService apiService;

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        String data = apiService.getApiData();
        response.getWriter().write(data);
    }
}