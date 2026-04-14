package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Sling Model for Checkout component.
 *
 * <p>This model adapts from {@link SlingHttpServletRequest} and is responsible for:
 * <ul>
 *     <li>Fetching cart data via {@link CartModel}</li>
 *     <li>Determining shipping cost based on request parameter</li>
 *     <li>Calculating subtotal and total values</li>
 * </ul>
 * </p>
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class CheckoutModel {

    Logger log= LoggerFactory.getLogger(CheckoutModel.class);

    @Self
    private SlingHttpServletRequest request;

    private CartModel cartModel;

    @ValueMapValue
    private double flatRate;

    @ValueMapValue
    private double pickupRate;

    private double shipping;

    /**
     * Initializes the model after injection.
     *
     * <p>
     * Adapts {@link CartModel} from the request and determines shipping cost
     * based on the "shipping" request parameter.
     * </p>
     */
    @PostConstruct
    protected void init() {
        log.info("============== CHECKOUT DEBUG START ===============");
        String sessionId = request.getSession().getId();
        log.info("Checkout Session ID: {}", sessionId);

        cartModel = request.adaptTo(CartModel.class);

        if (cartModel == null) {
            log.error("CartModel adaptation FAILED");
        } else {
            log.info("CartModel adapted successfully");
        }

        String shippingType = request.getParameter("shipping");

        if ("pickup".equals(shippingType)) {
            shipping = pickupRate;
        } else {
            shipping = flatRate;
        }

        log.info("Shipping Applied: {}", shipping);
    }

    /**
     * Returns cart items.
     *
     * @return list of {@link CartItem}, or null if cart not available
     */
    public List<CartItem> getItems() {
        return cartModel != null ? cartModel.getItems() : null;
    }

    /**
     * Returns subtotal from cart.
     *
     * @return subtotal amount
     */
    public double getSubtotal() {
        return cartModel != null ? cartModel.getSubtotal() : 0;
    }

    /**
     * Returns calculated shipping cost.
     *
     * @return shipping amount
     */
    public double getShipping() {
        return shipping;
    }

    /**
     * Calculates total amount (subtotal + shipping).
     *
     * @return total amount
     */
    public double getTotal() {
        return getSubtotal() + shipping;
    }
}