package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model extension of {@link ProductCFModel} that includes product tag support.
 *
 * This model is used to retrieve product-related tags authored in the content fragment
 * and expose them safely for frontend rendering or filtering purposes.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCFModelTag extends ProductCFModel {

    /**
     * Stores authored product tags associated with the product.
     */
    @ValueMapValue
    private String[] productTags;
        
    /**
     * Returns the list of product tags.
     * A cloned copy is returned to prevent external modification of the original array.
     *
     * @return array of product tags, or empty array if no tags are authored
     */
    public String[] getProductTags() {
        return productTags != null ? productTags.clone() : new String[0];
    }
}