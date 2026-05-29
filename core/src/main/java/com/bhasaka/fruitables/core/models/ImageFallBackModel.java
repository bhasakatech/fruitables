package com.bhasaka.fruitables.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;


@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ImageFallBackModel {

    private static final String FEATURED_IMAGE_NODE = "cq:featuredimage";
    private static final String FILE_REFERENCE = "fileReference";

    private static final String FALLBACK_IMAGE =
            "/content/dam/fruitables/Virat-Kohli.jpg";

    @ScriptVariable
    private Page currentPage;

    public String getMetaImagePath() {

        Page page = currentPage;

        while (page != null) {

            Resource featuredImageResource =
                    page.getContentResource(FEATURED_IMAGE_NODE);

            if (featuredImageResource != null) {

                String fileReference =
                        featuredImageResource.getValueMap()
                                .get(FILE_REFERENCE, String.class);

                if (StringUtils.isNotBlank(fileReference)) {
                    return fileReference;
                }
            }

            page = page.getParent();
        }

        return FALLBACK_IMAGE;
    }
}