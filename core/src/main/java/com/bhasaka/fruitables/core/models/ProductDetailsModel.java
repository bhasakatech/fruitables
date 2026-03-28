package com.bhasaka.fruitables.core.models;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentElement;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductDetailsModel {

    @SlingObject
    private ResourceResolver resolver;

    private String title;
    private String description;

    @Inject
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init() {

        String[] selectors = request.getRequestPathInfo().getSelectors();

        if (selectors != null && selectors.length > 0) {

            String product = selectors[0]; // orange

            String path = "/content/dam/fruitables/" + product;

            Resource resource = resolver.getResource(path);

            if (resource != null) {

                ContentFragment cf = resource.adaptTo(ContentFragment.class);

                if (cf != null) {

                    ContentElement titleEl = cf.getElement("title");
                    ContentElement descEl = cf.getElement("description");

                    if (titleEl != null) {
                        title = titleEl.getContent();
                    }

                    if (descEl != null) {
                        description = descEl.getContent();
                    }
                }
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}