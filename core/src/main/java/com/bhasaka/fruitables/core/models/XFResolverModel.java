package com.bhasaka.fruitables.core.models;

import com.adobe.cq.wcm.core.components.models.ExperienceFragment;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = ExperienceFragment.class,
        resourceType = "fruitables/components/experiencefragment",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class XFResolverModel implements ExperienceFragment {

    private static final String XF_PROPERTY = "xfpath";

    @ScriptVariable
    private Page currentPage;

    @Self
    @Via(type = ResourceSuperType.class)
    @Delegate(excludes = DelegationExclusion.class)
    private ExperienceFragment delegate;

    @Override

    public String getLocalizedFragmentVariationPath() {

        if (currentPage == null) {
            return delegate.getLocalizedFragmentVariationPath();
        }

        Resource contentResource = currentPage.getContentResource();

        if (contentResource == null) {
            return delegate.getLocalizedFragmentVariationPath();
        }

        InheritanceValueMap inheritanceMap =
                new HierarchyNodeInheritanceValueMap(contentResource);

        String xfPath = inheritanceMap.getInherited(
                XF_PROPERTY,
                String.class
        );

        if (StringUtils.isNotBlank(xfPath)) {
            return xfPath;
        }

        return delegate.getLocalizedFragmentVariationPath();
    }

    private interface DelegationExclusion {
        String getLocalizedFragmentVariationPath();
    }
}