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
 * Service class for handling product-related operations.
 * <p>
 * This class provides utility methods to fetch product data from
 * AEM Content Fragments stored under a specific DAM path.
 * </p>
 */
public class ProductService {
    /**
     * Retrieves a product Content Fragment based on the provided slug.
     * <p>
     * This method performs the following operations:
     * <ul>
     *     <li>Builds a query using QueryBuilder to fetch all content fragments
     *     under the specified DAM path.</li>
     *     <li>Iterates through each result (Hit).</li>
     *     <li>Adapts the resource to {@link ProductCFModel}.</li>
     *     <li>Generates a slug from the product name.</li>
     *     <li>Compares the generated slug with the input slug.</li>
     *     <li>Returns the matching product if found.</li>
     * </ul>
     * </p>
     *
     * @param resolver the {@link ResourceResolver} used to access AEM resources
     * @param slug the slug value used to identify the product
     * @return {@link ProductCFModel} if a matching product is found, otherwise {@code null}
     */
    public static ProductCFModel getProductBySlug(ResourceResolver resolver, String slug) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("path", "/content/dam/fruitables/products-list");
            map.put("type", "dam:Asset");
            map.put("p.limit", "-1");
            map.put("property", "jcr:content/contentFragment");
            map.put("property.value", "true");
            QueryBuilder builder = resolver.adaptTo(QueryBuilder.class);
            Session session = resolver.adaptTo(Session.class);
            Query query = builder.createQuery(PredicateGroup.create(map), session);
            SearchResult result = query.getResult();
            System.out.println("Total Hits: " + result.getHits().size());
            for (Hit hit : result.getHits()) {
                try {
                    Resource resource = resolver.getResource(hit.getPath());
                    if (resource == null) continue;
                    Resource master = resolver.getResource(
                            resource.getPath() + "/jcr:content/data/master"
                    );
                    if (master == null) continue;
                    ProductCFModel cf = master.adaptTo(ProductCFModel.class);
                    if (cf != null && cf.getProductName() != null) {
                        String generatedSlug = cf.getProductName()
                                .toLowerCase()
                                .trim()
                                .replaceAll("\\s+", "-")
                                .replaceAll("[^\\w-]", "");
                        if (slug.equals(generatedSlug)) {
                            return cf;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}