package com.bhasaka.fruitables.core.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import com.day.cq.wcm.api.Page;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = XFResolverService.class)
public class XFResolverServiceImpl implements XFResolverService {

    private static final String XF_PROPERTY = "xfpath";

    private static final Logger log = LoggerFactory.getLogger(XFResolverServiceImpl.class);

    @Override
    public String resolveXFPath(Page currentPage) {

        if (currentPage == null) {
            return null;
        }

        // 1. Current Page
        String xfPath = getXFfromPage(currentPage);
        if (StringUtils.isNotBlank(xfPath)) {
            return xfPath;
        }

        // 2. Parent Inheritance
        Page parent = currentPage.getParent();
        while (parent != null) {
            xfPath = getXFfromPage(parent);
            if (StringUtils.isNotBlank(xfPath)) {
                return xfPath;
            }
            parent = parent.getParent();
        }

        // 3. Template Fallback
        return getXFfromTemplate(currentPage);
    }

    private String getXFfromPage(Page page) {

        Resource contentResource = page.getContentResource();

        // 1. Check directly under jcr:content
        String xf = contentResource.getValueMap().get(XF_PROPERTY, String.class);
        if (StringUtils.isNotBlank(xf)) {
            return xf;
        }

        // 2. Check inside root (common case)
        Resource root = contentResource.getChild("root");
        if (root != null) {
            xf = findXFInChildren(root);
            if (StringUtils.isNotBlank(xf)) {
                return xf;
            }
        }

        return null;
    }

    private String findXFInChildren(Resource resource) {

        for (Resource child : resource.getChildren()) {

            String xf = child.getValueMap().get(XF_PROPERTY, String.class);
            if (StringUtils.isNotBlank(xf)) {
                return xf;
            }

            // recursive search
            xf = findXFInChildren(child);
            if (StringUtils.isNotBlank(xf)) {
                return xf;
            }
        }

        return null;
    }

    private String getXFfromTemplate(Page page) {

        if (page.getTemplate() == null) {
            return null;
        }

        String templatePath = page.getTemplate().getPath();

        // Editable template structure path
        String structurePath = templatePath + "/structure/jcr:content";

        Resource structureResource = page.getContentResource()
                .getResourceResolver()
                .getResource(structurePath);

        if (structureResource == null) {
            return null;
        }

        ValueMap props = structureResource.getValueMap();
        return props.get("xfpath", String.class);
    }
}