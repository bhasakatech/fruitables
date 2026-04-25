package com.bhasaka.fruitables.core.servlets;

import com.bhasaka.fruitables.core.service.CountryStateService;
import lombok.extern.slf4j.Slf4j;
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
                "sling.servlet.paths=/bin/states",
                "sling.servlet.methods=GET"
        }
)
@Slf4j
public class CountryStateServlet extends SlingSafeMethodsServlet {

    @Reference
    private CountryStateService service;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        log.info("CountryStateServlet doGet");
        String country = request.getParameter("country");

        response.setContentType("application/json");
        response.getWriter().write(service.getStates(country).toString());
    }
}