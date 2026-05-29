package com.bhasaka.fruitables.core.servlets;

import com.bhasaka.fruitables.core.service.HarshaApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/harsha",
                "sling.servlet.methods=GET"
        })
@Slf4j
public class HarshaApiServlet extends SlingSafeMethodsServlet {

    @Reference
    private HarshaApiService harshaApiService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        log.info("HarshaApiServlet doGet");
        log.error("HarshaApiServlet doGet Error");
        log.debug("HarshaApiServlet doGet Debug");
        // Get data from service
        Object data = harshaApiService.getData();

        // Convert to proper JSON
        String jsonResponse = objectMapper.writeValueAsString(data);

        response.getWriter().write(jsonResponse);
    }
}