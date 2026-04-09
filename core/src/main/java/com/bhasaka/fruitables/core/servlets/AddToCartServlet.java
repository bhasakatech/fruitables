package com.bhasaka.fruitables.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/cart/add",
                "sling.servlet.methods=POST"
        }
)
public class AddToCartServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(AddToCartServlet.class);

    @Reference
     ResourceResolverFactory factory;

    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response) throws IOException {

        String productPath = request.getParameter("productId");

        if (productPath == null || productPath.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Product ID is required\"}");
            return;
        }

        try (ResourceResolver resolver = factory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "fruitables-cart-service"))) {
log.info("=============================system-user===================="+resolver);
            String sessionId = request.getSession().getId();


            Resource cartBase = ensureCartBaseExists(resolver);

            String userCartPath = cartBase.getPath() + "/" + sessionId;
            Resource userCart = resolver.getResource(userCartPath);

            if (userCart == null) {
                userCart = resolver.create(cartBase, sessionId, new HashMap<>());
            }

            String itemName = productPath.substring(productPath.lastIndexOf("/") + 1);

            Resource item = resolver.getResource(userCart.getPath() + "/" + itemName);

            if (item == null) {
                Map<String, Object> props = new HashMap<>();
                props.put("productPath", productPath);
                props.put("quantity", 1);

                resolver.create(userCart, itemName, props);
            } else {
                ModifiableValueMap vm = item.adaptTo(ModifiableValueMap.class);
                int qty = vm.get("quantity", 0);
                vm.put("quantity", qty + 1);
            }

            resolver.commit();

            response.setContentType("application/json");
            response.setStatus(200);
            response.getWriter().write("{\"status\":\"success\", \"message\":\"Item added to cart\"}");

        } catch (Exception e) {
            log.error("Error while adding item to cart", e);
            response.setContentType("application/json");
            response.setStatus(500);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Internal server error\"}");
        }
    }


    private Resource ensureCartBaseExists(ResourceResolver resolver) throws PersistenceException {

        String basePath = "/content/usergenerated/cart";


        Resource base = resolver.getResource(basePath);
        if (base != null) {
            return base;
        }


        Resource usergenerated = resolver.getResource("/content/usergenerated");
        if (usergenerated == null) {
            Resource content = resolver.getResource("/content");
            usergenerated = resolver.create(content, "usergenerated", new HashMap<>());
        }


        base = resolver.create(usergenerated, "cart", new HashMap<>());

        return base;
    }
}