package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
class ProductCFModelTagTest {

    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductCFModelTag.class);
        context.load().json("/ProductCFModelTagTest.json", "/content");
    }

    @Test
    void testValidProductFieldsAndStarCollections() {
        ProductCFModelTag model = getModel("/content/product-valid");

        assertAll(
                () -> assertEquals("Apple Basket", model.getProductName()),
                () -> assertEquals("Fresh and crisp apples.", model.getProductDescription()),
                () -> assertEquals("Fruits", model.getProductCategory()),
                () -> assertEquals("/content/dam/fruitables/apple-basket.png", model.getProductImage()),
                () -> assertEquals("150", model.getProductPrice()),
                () -> assertEquals("kg", model.getUnit()),
                () -> assertEquals("4.2", model.getProductRating()),
                () -> assertArrayEquals(
                        new String[]{"fruitables:apples", "/content/cq:tags/fruitables/seasonal"},
                        model.getProductTags()
                ),
                () -> assertEquals(4, model.getFilledStars().size()),
                () -> assertEquals(1, model.getEmptyStars().size())
        );
    }

    @Test
    void testReturnedTagArrayIsDefensiveCopy() {
        ProductCFModelTag model = getModel("/content/product-valid");

        String[] productTags = model.getProductTags();
        productTags[0] = "changed:value";

        assertArrayEquals(
                new String[]{"fruitables:apples", "/content/cq:tags/fruitables/seasonal"},
                model.getProductTags()
        );
    }

    @Test
    void testInvalidAndBlankRatingsProduceNoFilledStars() {
        ProductCFModelTag invalidModel = getModel("/content/product-invalid");
        ProductCFModelTag blankModel = getModel("/content/product-blank");

        assertAll(
                () -> assertEquals(0, invalidModel.getFilledStars().size()),
                () -> assertEquals(5, invalidModel.getEmptyStars().size()),
                () -> assertEquals(0, blankModel.getFilledStars().size()),
                () -> assertEquals(5, blankModel.getEmptyStars().size()),
                () -> assertArrayEquals(new String[0], blankModel.getProductTags())
        );
    }

    private ProductCFModelTag getModel(String path) {
        context.currentResource(path);
        ProductCFModelTag model = context.currentResource().adaptTo(ProductCFModelTag.class);
        assertNotNull(model);
        return model;
    }
}
