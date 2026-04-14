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

/**
 * Sling Model for Cart component.
 *
 * <p>This model adapts from {@link SlingHttpServletRequest} and is responsible for:
 * <ul>
 *     <li>Fetching cart items from session-based storage</li>
 *     <li>Reading product details from Content Fragments</li>
 *     <li>Calculating subtotal, shipping, and total values</li>
 *     <li>Providing UI labels configured via dialog</li>
 * </ul>
 * </p>
 */
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

    /**
     * @return products table header
     */
    public String getProductsHeader() {
        return productsHeader;
    }

    /**
     * @return name column header
     */
    public String getNameHeader() {
        return nameHeader;
    }

    /**
     * @return price column header
     */
    public String getPriceHeader() {
        return priceHeader;
    }

    /**
     * @return quantity column header
     */
    public String getQuantityHeader() {
        return quantityHeader;
    }

    /**
     * @return total column header
     */
    public String getTotalHeaderTable() {
        return totalHeaderTable ;
    }

    /**
     * @return action/handle column header
     */
    public String getHandleHeader() {
        return handleHeader;
    }

    /**
     * @return checkout page link
     */
    public String getCheckoutLink() {
        return checkoutLink;
    }

    /**
     * @return subtotal label text
     */
    public String getSubtotalLabel() {
        return subtotalLabel;
    }

    /**
     * @return shipping label text
     */
    public String getShippingLabel() {
        return shippingLabel;
    }

    /**
     * @return total label text
     */
    public String getTotalLabel() {
        return totalLabel;
    }

    /**
     * @return checkout button text
     */
    public String getCheckoutBtnText() {
        return checkoutBtnText;
    }

    /**
     * @return coupon input placeholder text
     */
    public String getCouponPlaceholder() {
        return couponPlaceholder;
    }

    /**
     * @return coupon button text
     */
    public String getCouponButtonText() {
        return couponButtonText ;
    }

    /**
     * Fetches cart items for the current session.
     *
     * <p>Reads cart data from:
     * <code>/content/usergenerated/cart/{sessionId}</code></p>
     *
     * <p>For each item:
     * <ul>
     *     <li>Reads product path and quantity</li>
     *     <li>Fetches product details from Content Fragment</li>
     *     <li>Builds {@link CartItem} object</li>
     * </ul>
     * </p>
     *
     * <p>Invalid products are skipped and default image is used if missing.</p>
     *
     * @return list of cart items (empty if none found)
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
                image = "/content/dam/default.png";
            }

            item.setImage(image);
            item.setQty(qty);
            item.setProductPath(productPath);

            list.add(item);
        }
        return list;
    }

    /**
     * Calculates subtotal of all cart items.
     *
     * @return subtotal amount
     */
    public double getSubtotal() {
        return getItems().stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    /**
     * Returns shipping cost.
     *
     * <p>Returns 0 if cart is empty, otherwise fixed shipping charge.</p>
     *
     * @return shipping cost
     */
    public double getShipping() {
        List<CartItem> items = getItems();

        if (items == null || items.isEmpty()) {
            return 0.0;
        }
        return 3.0;
    }

    /**
     * Calculates total amount (subtotal + shipping).
     *
     * @return total amount
     */
    public double getTotal() {
        return getSubtotal() + getShipping();
    }
}