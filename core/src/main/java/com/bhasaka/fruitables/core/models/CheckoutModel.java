package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

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

    public List<CartItem> getItems() {
        return cartModel != null ? cartModel.getItems() : null;
    }

    public double getSubtotal() {
        return cartModel != null ? cartModel.getSubtotal() : 0;
    }

    public double getShipping() {
        return shipping;
    }

    public double getTotal() {
        return getSubtotal() + shipping;
    }
}