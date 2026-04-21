package com.bhasaka.fruitables.core.service;

import com.bhasaka.fruitables.core.models.ProductCFModel;
import com.day.cq.search.*;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.*;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling product-related operations.
 * <p>
 * This class provides methods to fetch product Content Fragments
 * from AEM DAM based on a generated slug.
 * </p>
 */
public final class ProductService {

    /**
     * Private constructor to prevent instantiation.
     */
    private ProductService() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Retrieves a {@link ProductCFModel} based on the given slug.
     * <p>
     * This method:
     * <ul>
     *     <li>Builds a QueryBuilder query to fetch content fragments</li>
     *     <li>Iterates through results</li>
     *     <li>Generates slug from product name</li>
     *     <li>Returns matching product</li>
     * </ul>
     *
     * @param resolver the {@link ResourceResolver} to access repository resources
     * @param slug the slug used to identify the product
     * @return matching {@link ProductCFModel} or {@code null} if not found
     */
    public static ProductCFModel getProductBySlug(ResourceResolver resolver, String slug) {
        try {
            Query query = buildQuery(resolver);
            SearchResult result = query.getResult();

            for (Hit hit : result.getHits()) {
                ProductCFModel product = processHit(resolver, hit, slug);
                if (product != null) {
                    return product;
                }
            }
        } catch (Exception e) {
            // Exception intentionally ignored
        }
        return null;
    }

    /**
     * Builds the QueryBuilder query to fetch product content fragments.
     *
     * @param resolver the {@link ResourceResolver}
     * @return the constructed {@link Query}
     */
    private static Query buildQuery(ResourceResolver resolver) {
        Map<String, String> map = new HashMap<>();
        map.put("path", "/content/dam/fruitables/products-list");
        map.put("type", "dam:Asset");
        map.put("p.limit", "-1");
        map.put("property", "jcr:content/contentFragment");
        map.put("property.value", "true");

        QueryBuilder builder = resolver.adaptTo(QueryBuilder.class);
        Session session = resolver.adaptTo(Session.class);

        return builder.createQuery(PredicateGroup.create(map), session);
    }

    /**
     * Processes a single search hit and checks if it matches the slug.
     *
     * @param resolver the {@link ResourceResolver}
     * @param hit the {@link Hit} from search results
     * @param slug the slug to match
     * @return matching {@link ProductCFModel} or {@code null}
     */
    private static ProductCFModel processHit(ResourceResolver resolver, Hit hit, String slug) {
        try {
            Resource resource = resolver.getResource(hit.getPath());
            if (resource == null) return null;

            Resource master = resolver.getResource(resource.getPath() + "/jcr:content/data/master");
            if (master == null) return null;

            ProductCFModel cf = master.adaptTo(ProductCFModel.class);

            if (cf != null && cf.getProductName() != null) {
                String generatedSlug = generateSlug(cf.getProductName());
                if (slug.equals(generatedSlug)) {
                    return cf;
                }
            }
        } catch (Exception e) {
            // Exception intentionally ignored
        }
        return null;
    }

    /**
     * Generates a URL-friendly slug from the given product name.
     *
     * @param productName the product name
     * @return generated slug string
     */
    private static String generateSlug(String productName) {
        return productName.toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^\\w-]", "");
    }
}