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

/**
 * Servlet responsible for handling Add-To-Cart functionality.
 *
 * <p>This servlet receives a product path from the client request,
 * normalizes it (to remove unwanted subpaths like jcr:content or master nodes),
 * and stores the product in a session-based cart under
 * /content/usergenerated/cart.</p>
 *
 * <p>If the cart does not exist for the session, it will be created.
 * If the product already exists in the cart, its quantity is incremented.</p>
 *
 * <p>Supported Path Formats:
 * <ul>
 *     <li>/content/dam/.../product</li>
 *     <li>/content/dam/.../product/jcr:content</li>
 *     <li>/content/dam/.../product/jcr:content/data/master</li>
 * </ul>
 * All the above will be normalized to the base product path.</p>
 *
 * <p>Endpoint: POST /bin/cart/add</p>
 */

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

    /**
     * Handles POST request to add a product to the cart.
     *
     * <p>Steps:
     * <ol>
     *     <li>Read productId parameter from request</li>
     *     <li>Normalize the product path</li>
     *     <li>Get or create user session cart</li>
     *     <li>Add product or update quantity</li>
     *     <li>Commit changes to repository</li>
     * </ol>
     * </p>
     *
     * @param request  Sling HTTP request containing productId parameter
     * @param response Sling HTTP response returning JSON status
     * @throws IOException if response writing fails
     */
    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response) throws IOException {

        String rawProductPath = request.getParameter("productId");

        if (rawProductPath == null || rawProductPath.isEmpty()) {
            sendError(response, 400, "Product ID is required");
            return;
        }

        try (ResourceResolver resolver = factory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "fruitables-cart-service"))) {
            String productPath = normalizeProductPath(rawProductPath);

            log.info("Final Product Path: {}", productPath);

            String sessionId = request.getSession().getId();

            Resource cartBase = ensureCartBaseExists(resolver);

            Resource userCart = resolver.getResource(cartBase.getPath() + "/" + sessionId);

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

            sendSuccess(response, "Item added to cart");

        } catch (Exception e) {
            log.error("Error while adding item to cart", e);
            sendError(response, 500, "Internal server error");
        }
    }

    /**
     * Normalizes the product path by removing unwanted repository nodes.
     *
     * <p>This method ensures consistency across different components
     * that may send varying path formats.</p>
     *
     * <p>Examples:
     * <ul>
     *     <li>/product/jcr:content/data/master → /product</li>
     *     <li>/product/jcr:content → /product</li>
     *     <li>/product → /product (unchanged)</li>
     * </ul>
     * </p>
     *
     * @param path Raw product path from request
     * @return Normalized base product path
     */
    private String normalizeProductPath(String path) {

        if (path == null) return null;
        if (path.contains("/jcr:content/data/master")) {
            return path.substring(0, path.indexOf("/jcr:content"));
        }
        if (path.contains("/jcr:content")) {
            return path.substring(0, path.indexOf("/jcr:content"));
        }
        return path;
    }

    /**
     * Ensures that the base cart structure exists in the repository.
     *
     * <p>Creates the following structure if missing:
     * /content/usergenerated/cart</p>
     *
     * @param resolver ResourceResolver with write permissions
     * @return Resource representing the cart base path
     * @throws PersistenceException if node creation fails
     */
    private Resource ensureCartBaseExists(ResourceResolver resolver) throws PersistenceException {

        String basePath = "/content/usergenerated/cart";

        Resource base = resolver.getResource(basePath);
        if (base != null) return base;

        Resource usergenerated = resolver.getResource("/content/usergenerated");
        if (usergenerated == null) {
            Resource content = resolver.getResource("/content");
            usergenerated = resolver.create(content, "usergenerated", new HashMap<>());
        }

        return resolver.create(usergenerated, "cart", new HashMap<>());
    }

    /**
     * Sends a success JSON response.
     *
     * @param response Sling response
     * @param message  Success message
     * @throws IOException if writing response fails
     */
    private void sendSuccess(SlingHttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().write("{\"status\":\"success\",\"message\":\"" + message + "\"}");
    }

    /**
     * Sends an error JSON response.
     *
     * @param response Sling response
     * @param status   HTTP status code
     * @param message  Error message
     * @throws IOException if writing response fails
     */
    private void sendError(SlingHttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write("{\"status\":\"error\",\"message\":\"" + message + "\"}");
    }
}