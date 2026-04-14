package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model representing a product resource configuration.
 *
 * <p>
 * This model adapts from a {@link Resource} and is used to retrieve
 * author-configured properties for a product item.
 * </p>
 *
 * <p>
 * It primarily provides:
 * <ul>
 * <li>The Content Fragment path of the product</li>
 * <li>The card style used for rendering the product</li>
 * </ul>
 * </p>
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductResource {

    /**
     * Path to the Content Fragment representing the product.
     */
    @ValueMapValue
    private String cfPath;

    /**
     * Style applied to the product card (e.g., compact, detailed).
     */
    @ValueMapValue
    private String cardStyle;

    /**
     * Returns the Content Fragment path of the product.
     *
     * @return the product Content Fragment path
     */
    public String getCfPath() {
        return cfPath;
    }

    /**
     * Returns the card style configured for the product.
     *
     * @return the card style
     */
    public String getCardStyle() {
        return cardStyle;
    }
}
