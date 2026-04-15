package com.bhasaka.fruitables.core.service;
import com.bhasaka.fruitables.core.models.ProductCFModel;
import com.day.cq.search.*;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.*;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;
public class ProductService {
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