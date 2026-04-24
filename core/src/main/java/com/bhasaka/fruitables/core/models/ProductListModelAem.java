package com.bhasaka.fruitables.core.models;

import com.bhasaka.fruitables.core.entities.Product;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListModelAem {

    @ChildResource(name = "products")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }
}