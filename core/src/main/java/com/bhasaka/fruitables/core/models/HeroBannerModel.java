package com.bhasaka.fruitables.core.models;

import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.api.resource.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Sling Model for the Hero Banner component.
 *
 * <p>This model adapts from a {@link Resource} and is used to fetch
 * properties authored in the Hero Banner component in AEM.</p>
 *
 * <p>It provides access to:
 * <ul>
 *     <li>Subtitle text</li>
 *     <li>Title text</li>
 *     <li>Background image path</li>
 *     <li>List of slides (child resources)</li>
 * </ul>
 * </p>
 *
 * <p>If no slides are authored, an empty list is returned to avoid null checks.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "fruitables/components/hero-banner"
)
public class HeroBannerModel {

    /**
     * List of slide items authored as child resources under the component.
     */
    @ChildResource
    private List<SlideModel> slides;

    /**
     * Subtitle text for the hero banner.
     */
    @ValueMapValue
    private String subtitle;

    /**
     * Main title text for the hero banner.
     */
    @ValueMapValue
    private String title;

    /**
     * Path to the background image selected in the dialog.
     */
    @ValueMapValue
    private String backgroundImage;

    /**
     * Returns the background image path.
     *
     * @return the background image path, or null if not authored
     */
    public String getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Returns the subtitle of the hero banner.
     *
     * @return subtitle text, or null if not authored
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Returns the title of the hero banner.
     *
     * @return title text, or null if not authored
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the list of slides.
     *
     * <p>If no slides are available, returns an empty list instead of null
     * to avoid NullPointerException.</p>
     *
     * @return list of {@link SlideModel} objects, never null
     */
    public List<SlideModel> getSlides() {
        return slides != null ? slides : Collections.emptyList();
    }
}