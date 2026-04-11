package com.bhasaka.fruitables.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

final class ProductCategorySupport {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategorySupport.class);

    private ProductCategorySupport() {
    }

    static List<Resource> queryProductMasterResources(ResourceResolver resourceResolver, String fragmentRootPath) {
        if (resourceResolver == null || isBlank(fragmentRootPath)) {
            return Collections.emptyList();
        }

        Session session = resourceResolver.adaptTo(Session.class);
        if (session == null) {
            return Collections.emptyList();
        }

        List<Resource> masterResources = new ArrayList<>();
        try {
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(buildSql2Statement(fragmentRootPath), Query.JCR_SQL2);
            NodeIterator nodeIterator = query.execute().getNodes();

            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.nextNode();
                Resource masterResource = resourceResolver.getResource(node.getPath());
                if (masterResource != null) {
                    masterResources.add(masterResource);
                }
            }
        } catch (RepositoryException e) {
            LOG.error("Unable to query Content Fragment master nodes below {}", fragmentRootPath, e);
        }

        return masterResources;
    }

    static Set<String> extractProductTagIds(ResourceResolver resourceResolver, ValueMap valueMap, ProductCFModelTag product) {
        Set<String> productTagIds = new LinkedHashSet<>();
        addNormalizedTags(resourceResolver, productTagIds, valueMap.get("productTags", String[].class));
        addNormalizedTag(resourceResolver, productTagIds, valueMap.get("productTags", String.class));

        if (product != null) {
            addNormalizedTags(resourceResolver, productTagIds, product.getProductTags());
        }

        if (productTagIds.isEmpty()) {
            addNormalizedTags(resourceResolver, productTagIds, valueMap.get("cq:tags", String[].class));
            addNormalizedTag(resourceResolver, productTagIds, valueMap.get("cq:tags", String.class));
        }

        return productTagIds;
    }

    static String normalizeTagId(ResourceResolver resourceResolver, String value) {
        if (isBlank(value)) {
            return null;
        }

        TagManager tagManager = resourceResolver != null ? resourceResolver.adaptTo(TagManager.class) : null;
        if (tagManager != null) {
            Tag tag = tagManager.resolve(value);
            if (tag != null && !isBlank(tag.getTagID())) {
                return tag.getTagID();
            }
        }

        String trimmedValue = value.trim();
        if (trimmedValue.startsWith("/content/cq:tags/")) {
            return trimmedValue.substring("/content/cq:tags/".length()).replaceFirst("/", ":");
        }
        if (trimmedValue.startsWith("/etc/tags/")) {
            return trimmedValue.substring("/etc/tags/".length()).replaceFirst("/", ":");
        }
        return trimmedValue;
    }

    static String resolveCategoryTitle(ResourceResolver resourceResolver, String tagValue) {
        TagManager tagManager = resourceResolver != null ? resourceResolver.adaptTo(TagManager.class) : null;
        if (tagManager != null) {
            Tag tag = tagManager.resolve(tagValue);
            if (tag != null) {
                if (!isBlank(tag.getTitle())) {
                    return tag.getTitle();
                }
                if (!isBlank(tag.getName())) {
                    return humanize(tag.getName());
                }
            }
        }

        return humanize(extractTagLeaf(resourceResolver, tagValue));
    }

    static Resource findComponentOnPage(Resource componentResource, String resourceType) {
        Resource pageContentResource = findContainingPageContentResource(componentResource);
        if (pageContentResource == null) {
            return null;
        }
        return findByResourceType(pageContentResource, resourceType);
    }

    static String buildComponentId(Resource resource, String prefix) {
        return resource != null
                ? prefix + Math.abs(resource.getPath().hashCode())
                : prefix.substring(0, prefix.length() - 1);
    }

    static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static void addNormalizedTags(ResourceResolver resourceResolver, Set<String> target, String[] values) {
        if (values == null) {
            return;
        }
        for (String value : values) {
            addNormalizedTag(resourceResolver, target, value);
        }
    }

    private static void addNormalizedTag(ResourceResolver resourceResolver, Set<String> target, String value) {
        String normalizedTagId = normalizeTagId(resourceResolver, value);
        if (!isBlank(normalizedTagId)) {
            target.add(normalizedTagId);
        }
    }

    private static String buildSql2Statement(String fragmentRootPath) {
        return "SELECT * FROM [nt:unstructured] AS master "
                + "WHERE ISDESCENDANTNODE(master, '" + escapeSql2Literal(fragmentRootPath) + "') "
                + "AND NAME(master) = 'master' "
                + "AND (master.[productTitle] IS NOT NULL OR master.[productName] IS NOT NULL)";
    }

    private static String escapeSql2Literal(String value) {
        return value.replace("'", "''");
    }

    private static Resource findContainingPageContentResource(Resource resource) {
        Resource current = resource;
        while (current != null) {
            if ("jcr:content".equals(current.getName())) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private static Resource findByResourceType(Resource root, String resourceType) {
        if (root == null) {
            return null;
        }
        if (root.isResourceType(resourceType)) {
            return root;
        }
        for (Resource child : root.getChildren()) {
            Resource match = findByResourceType(child, resourceType);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    private static String extractTagLeaf(ResourceResolver resourceResolver, String tagValue) {
        String normalizedTagId = normalizeTagId(resourceResolver, tagValue);
        if (isBlank(normalizedTagId)) {
            return "";
        }

        if (normalizedTagId.contains(":")) {
            normalizedTagId = normalizedTagId.substring(normalizedTagId.indexOf(':') + 1);
        }
        int lastSlashIndex = normalizedTagId.lastIndexOf('/');
        return lastSlashIndex >= 0 ? normalizedTagId.substring(lastSlashIndex + 1) : normalizedTagId;
    }

    private static String humanize(String value) {
        if (isBlank(value)) {
            return "";
        }

        String normalized = value.replace('-', ' ').replace('_', ' ').trim();
        String[] words = normalized.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(word.substring(0, 1).toUpperCase(Locale.ENGLISH));
            if (word.length() > 1) {
                builder.append(word.substring(1));
            }
        }
        return builder.toString();
    }
}
