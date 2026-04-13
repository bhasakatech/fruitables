package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CartModel {

    private static final Logger log = LoggerFactory.getLogger(CartModel.class);

    @org.apache.sling.models.annotations.injectorspecific.SlingObject
    private SlingHttpServletRequest request;


    @Inject
    private ResourceResolver resolver;

    @ValueMapValue
    private String checkoutLink;

    public String getCheckoutLink() {
        return checkoutLink;
    }

    public List<CartItem> getItems() {

        List<CartItem> list = new ArrayList<>();

        String sessionId = request.getSession().getId();
        String path = "/content/usergenerated/cart/" + sessionId;
        log.info("=== CART DEBUG START ===");
        log.info("Session ID: {}", sessionId);
        log.info("Cart Path: {}", path);

        Resource cartRes = resolver.getResource(path);

        if (cartRes == null) {
            log.warn("Cart not found for session: {}", sessionId);
            return list;
        }
        log.info("Cart Resource FOUND: {}", cartRes.getPath());

        for (Resource child : cartRes.getChildren()) {
            ValueMap vm = child.getValueMap();
            String productPath = vm.get("productPath", "");
            int qty = vm.get("quantity", 0);
            log.info("Item Found -> ProductPath: {}, Qty: {}", productPath, qty);
            Resource productRes = resolver.getResource(productPath);
            if (productRes == null) {
                log.warn("Product not found: {}", productPath);
                continue;
            }
            Resource dataRes = productRes.getChild("jcr:content/data/master");
            if (dataRes == null) {
                log.warn("CF data missing for: {}", productPath);
                continue;
            }
            ValueMap pvm = dataRes.getValueMap();
            CartItem item = new CartItem();
            item.setName(pvm.get("productName", ""));
            Object priceObj = pvm.get("productPrice");
            double price = 0.0;
            if (priceObj instanceof Number) {
                price = ((Number) priceObj).doubleValue();
            } else if (priceObj instanceof String) {
                try {
                    price = Double.parseDouble((String) priceObj);
                } catch (Exception e) {
                    log.warn("Invalid price format for {}: {}", productPath, priceObj);
                }
            }
            item.setPrice(price);
            String image = pvm.get("productImage", String.class);
            if (image == null || image.isEmpty()) {
                image = "/content/dam/default.png"; // fallback image
            }
            item.setImage(image);
            item.setQty(qty);
            item.setProductPath(productPath);
            list.add(item);
        }
        return list;
    }
    public double getSubtotal() {
        return getItems().stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    public double getShipping() {
        List<CartItem> items = getItems();

        if (items == null || items.isEmpty()) {
            return 0.0;
        }
        return 3.0;
    }

    public double getTotal() {
        return getSubtotal() + getShipping();
    }
}