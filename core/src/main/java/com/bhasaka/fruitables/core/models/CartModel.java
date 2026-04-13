package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CartModel {

    private static final Logger log = LoggerFactory.getLogger(CartModel.class);

    @SlingObject
    private SlingHttpServletRequest request;


    @Inject
    private ResourceResolver resolver;

    @ValueMapValue
    private String checkoutLink;

    @ValueMapValue
    private String subtotalLabel;

    @ValueMapValue
    private String shippingLabel;

    @ValueMapValue
    private String totalLabel;

    @ValueMapValue
    private String checkoutBtnText;

    @ValueMapValue
    private String couponPlaceholder;

    @ValueMapValue
    private String couponButtonText;

    @ValueMapValue
    private String productsHeader;

    @ValueMapValue
    private String nameHeader;

    @ValueMapValue
    private String priceHeader;

    @ValueMapValue
    private String quantityHeader;

    @ValueMapValue
    private String totalHeaderTable;

    @ValueMapValue
    private String handleHeader;

    public String getProductsHeader() {
        return productsHeader;
    }

    public String getNameHeader() {
        return nameHeader;
    }

    public String getPriceHeader() {
        return priceHeader;
    }

    public String getQuantityHeader() {
        return quantityHeader;
    }

    public String getTotalHeaderTable() {
        return totalHeaderTable ;
    }

    public String getHandleHeader() {
        return handleHeader;
    }

    public String getCheckoutLink() {
        return checkoutLink;
    }
    public String getSubtotalLabel() {
        return subtotalLabel;
    }

    public String getShippingLabel() {
        return shippingLabel;
    }

    public String getTotalLabel() {
        return totalLabel;
    }

    public String getCheckoutBtnText() {
        return checkoutBtnText;
    }

    public String getCouponPlaceholder() {
        return couponPlaceholder;
    }

    public String getCouponButtonText() {
        return couponButtonText ;
    }

    /**
     * Fetches cart items for the current session from
     * /content/usergenerated/cart/{sessionId}.
     *
     * For each entry, it reads product details (name, price, image)
     * from the corresponding Content Fragment and builds a CartItem.
     *
     * Skips invalid or missing products. Uses default image if not available.
     *
     * @return list of cart items, or empty list if cart not found
     */
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