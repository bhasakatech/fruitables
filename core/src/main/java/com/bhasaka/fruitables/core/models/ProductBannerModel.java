package com.bhasaka.fruitables.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

/**
 * Sling Model for the Product Banner component.
 *
 * <p>This model adapts from a {@link Resource} and is used to retrieve
 * product-related data authored in the AEM dialog for rendering
 * the Product Banner component.</p>
 *
 * <p>The model includes:
 * <ul>
 *     <li>Product heading and subheading</li>
 *     <li>Description text</li>
 *     <li>CTA button label and link</li>
 *     <li>Product image path</li>
 *     <li>Pricing details (main price, decimal price, currency, unit)</li>
 * </ul>
 * </p>
 *
 * <p>Lombok's {@code @Getter} annotation is used to automatically generate
 * getter methods for all fields.</p>
 */
@Model(
        adaptables = Resource.class,
        adapters = ProductBannerModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Getter
public class ProductBannerModel {

    /**
     * Main heading of the product banner.
     */
    @ValueMapValue
    private String heading;

    /**
     * Subheading text displayed below the main heading.
     */
    @ValueMapValue
    private String subheading;

    /**
     * Description of the product.
     */
    @ValueMapValue
    private String description;

    /**
     * Label text for the call-to-action button.
     */
    @ValueMapValue
    private String buttonLabel;

    /**
     * URL or path the button redirects to.
     */
    @ValueMapValue
    private String buttonLink;

    /**
     * Path to the product image.
     */
    @ValueMapValue
    private String productImage;

    /**
     * Integer part of the product price.
     */
    @ValueMapValue
    private String mainPrice;

    /**
     * Decimal part of the product price.
     */
    @ValueMapValue
    private String decimalPrice;

    /**
     * Currency symbol or code (e.g., INR, $, €).
     */
    @ValueMapValue
    private String currency;

    /**
     * Unit of the product (e.g., kg, piece).
     */
    @ValueMapValue
    private String unit;

    /**
     * Initialization method invoked after all injections are completed.
     *
     * <p>Currently not performing any logic, but can be used for
     * data transformation or validation if needed.</p>
     */
    @PostConstruct
    protected void init() {}
}