package com.bhasaka.fruitables.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.tagging.TagConstants;
import com.day.cq.commons.jcr.JcrConstants;

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

/**
 * Utility support class for handling product category, tags, and component-related operations.
 *
 * This class provides helper methods for:
 * querying product content fragment master resources,
 * extracting/normalizing tags,
 * resolving category titles,
 * and locating components/resources in page hierarchy.
 */
final class ProductCategorySupport {

    /**
     * Logger instance for ProductCategorySupport.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ProductCategorySupport.class);

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ProductCategorySupport() {
    }

    /**
     * Queries product master resources from the given content fragment root path.
     *
     * @param resourceResolver resource resolver instance
     * @param fragmentRootPath root path of content fragments
     * @return list of product master resources
     */
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

    /**
     * Extracts product tag IDs from product and value map.
     *
     * @param resourceResolver resource resolver instance
     * @param valueMap resource properties map
     * @param product product content fragment model
     * @return normalized set of product tag IDs
     */
    static Set<String> extractProductTagIds(ResourceResolver resourceResolver, ValueMap valueMap, ProductCFModelTag product) {
        Set<String> productTagIds = new LinkedHashSet<>();
        addNormalizedTags(resourceResolver, productTagIds, valueMap.get(TagConstants.PN_TAGS, String[].class));
        addNormalizedTag(resourceResolver, productTagIds, valueMap.get(TagConstants.PN_TAGS, String.class));

        if (product != null) {
            addNormalizedTags(resourceResolver, productTagIds, product.getProductTags());
        }

        if (productTagIds.isEmpty()) {
            addNormalizedTags(resourceResolver, productTagIds, valueMap.get("cq:tags", String[].class));
            addNormalizedTag(resourceResolver, productTagIds, valueMap.get("cq:tags", String.class));
        }

        return productTagIds;
    }

    /**
     * Normalizes tag value into standard tag ID format.
     *
     * @param resourceResolver resource resolver instance
     * @param value tag value/path
     * @return normalized tag ID
     */
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

    /**
     * Resolves readable category title from tag value.
     *
     * @param resourceResolver resource resolver instance
     * @param tagValue tag value
     * @return resolved category title
     */
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

    /**
     * Finds component on page by resource type.
     *
     * @param componentResource component resource
     * @param resourceType target resource type
     * @return matching component resource
     */
    static Resource findComponentOnPage(Resource componentResource, String resourceType) {
        Resource pageContentResource = findContainingPageContentResource(componentResource);
        if (pageContentResource == null) {
            return null;
        }
        return findByResourceType(pageContentResource, resourceType);
    }

    /**
     * Builds unique component ID using resource path hash.
     *
     * @param resource resource instance
     * @param prefix ID prefix
     * @return generated component ID
     */
    static String buildComponentId(Resource resource, String prefix) {
        if (resource == null) {
            return prefix;
        }
        return prefix + Math.abs(resource.getPath().hashCode());
    }

    /**
     * Checks whether string is null or blank.
     *
     * @param value input string
     * @return true if blank, otherwise false
     */
    static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Adds normalized tags into target set.
     *
     * @param resourceResolver resource resolver instance
     * @param target target set
     * @param values tag values
     */
    private static void addNormalizedTags(ResourceResolver resourceResolver, Set<String> target, String[] values) {
        if (values == null) {
            return;
        }
        for (String value : values) {
            addNormalizedTag(resourceResolver, target, value);
        }
    }

    /**
     * Adds single normalized tag into target set.
     *
     * @param resourceResolver resource resolver instance
     * @param target target set
     * @param value tag value
     */
    private static void addNormalizedTag(ResourceResolver resourceResolver, Set<String> target, String value) {
        String normalizedTagId = normalizeTagId(resourceResolver, value);
        if (!isBlank(normalizedTagId)) {
            target.add(normalizedTagId);
        }
    }

    /**
     * Builds SQL2 query statement for content fragment lookup.
     *
     * @param fragmentRootPath fragment root path
     * @return SQL2 query string
     */
    private static String buildSql2Statement(String fragmentRootPath) {
        return "SELECT * FROM [nt:unstructured] AS master "
                + "WHERE ISDESCENDANTNODE(master, '" + escapeSql2Literal(fragmentRootPath) + "') "
                + "AND NAME(master) = 'master' "
                + "AND ((master.[productTitle] IS NOT NULL) OR (master.[productName] IS NOT NULL))";
    }

    /**
     * Escapes SQL2 literals for safe query usage.
     *
     * @param value input string
     * @return escaped SQL2 string
     */
    private static String escapeSql2Literal(String value) {
        return value.replace("'", "''");
    }

    /**
     * Finds containing jcr:content resource from hierarchy.
     *
     * @param resource starting resource
     * @return jcr:content resource
     */
    private static Resource findContainingPageContentResource(Resource resource) {
        Resource current = resource;
        while (current != null) {
            if (JcrConstants.JCR_CONTENT.equals(current.getName())) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    /**
     * Recursively finds resource by resource type.
     *
     * @param root root resource
     * @param resourceType target resource type
     * @return matching resource
     */
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

    /**
     * Extracts leaf portion from tag value.
     *
     * @param resourceResolver resource resolver instance
     * @param tagValue tag value
     * @return extracted tag leaf
     */
    private static String extractTagLeaf(ResourceResolver resourceResolver, String tagValue) {
        final String normalizedTagId = normalizeTagId(resourceResolver, tagValue);

        if (isBlank(normalizedTagId)) {
            return "";
        }

        String tagWithoutNamespace = normalizedTagId.contains(":")
                ? normalizedTagId.substring(normalizedTagId.indexOf(':') + 1)
                : normalizedTagId;

        int lastSlashIndex = tagWithoutNamespace.lastIndexOf('/');
        return lastSlashIndex >= 0
                ? tagWithoutNamespace.substring(lastSlashIndex + 1)
                : tagWithoutNamespace;
    }

    /**
     * Converts raw string into human-readable title format.
     *
     * @param value input text
     * @return formatted text
     */
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