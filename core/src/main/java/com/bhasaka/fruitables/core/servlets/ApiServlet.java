package com.bhasaka.fruitables.core.servlets;

import com.bhasaka.fruitables.core.service.ApiService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/api-data",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=GET"
        }
)
public class ApiServlet extends SlingAllMethodsServlet {

    @Reference
    private ApiService service;

    @Override
    protected void doGet( SlingHttpServletRequest request,  SlingHttpServletResponse response) throws ServletException, IOException {
        String apiData = service.getApiData();
        response.setContentType("application/json");
        response.getWriter().write(apiData);
    }
}
