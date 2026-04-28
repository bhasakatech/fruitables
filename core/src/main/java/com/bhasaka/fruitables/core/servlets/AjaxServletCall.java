package com.bhasaka.fruitables.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.resourceTypes=fruitables/components/practiceJS",
                "sling.servlet.methods=GET",
                "sling.servlet.extensions=json"
        }
)
public class AjaxServletCall extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AjaxServletCall.class);
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOG.info("AjaxServletCall servlet invoked");
        response.getWriter().write("AjaxCall servlet response");
    }
}