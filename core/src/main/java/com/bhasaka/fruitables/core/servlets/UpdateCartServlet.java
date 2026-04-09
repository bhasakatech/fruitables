package com.bhasaka.fruitables.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/cart/update",
                "sling.servlet.methods=POST"
        }
)
public class UpdateCartServlet extends SlingAllMethodsServlet {

    @Reference
    private ResourceResolverFactory factory;

    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response) throws IOException {

        String productPath = request.getParameter("productPath");
        String action = request.getParameter("action");

        try {
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "fruitables-cart-service");

            try (ResourceResolver resolver = factory.getServiceResourceResolver(param)) {

                String sessionId = request.getSession().getId();
                String itemName = productPath.substring(productPath.lastIndexOf("/") + 1)
                        .replaceAll("[^a-zA-Z0-9-]", "-")
                        .toLowerCase();

                String itemPath = "/content/usergenerated/cart/" + sessionId + "/" + itemName;

                Resource item = resolver.getResource(itemPath);

                if (item == null) return;

                if ("delete".equals(action)) {
                    resolver.delete(item);
                } else {

                    ModifiableValueMap vm = item.adaptTo(ModifiableValueMap.class);
                    int qty = vm.get("quantity", 0);

                    if ("inc".equals(action)) {
                        vm.put("quantity", qty + 1);
                    } else if ("dec".equals(action)) {
                        if (qty <= 1) {
                            resolver.delete(item);
                        } else {
                            vm.put("quantity", qty - 1);
                        }
                    }
                }

                resolver.commit();
            }

        } catch (Exception e) {
            response.setStatus(500);
        }
    }
}