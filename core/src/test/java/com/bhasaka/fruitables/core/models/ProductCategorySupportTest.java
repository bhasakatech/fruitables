package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link ProductCategorySupport}.
 *
 * This class validates product category support utility methods including
 * query execution, tag extraction, component lookup,
 * tag normalization, and category title resolution.
 */
@ExtendWith(AemContextExtension.class)
class ProductCategorySupportTest {

    /**
     * AEM mock context used for Sling/AEM unit testing.
     */
    private final AemContext context = new AemContext();

    /**
     * Initializes mock AEM context before each test execution.
     *
     * Loads test JSON content and registers Sling models.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductCFModelTag.class);
        context.load().json("/productCategorySupportTest.json", "/content");
    }

    /**
     * Tests querying product master resources from configured fragment root path.
     *
     * Verifies that returned resources match expected master nodes.
     *
     * @throws Exception if mock query setup fails
     */
    @Test
    void testQueryProductMasterResourcesReturnsMasterNodesBelowConfiguredRoot() throws Exception {
        Resource appleMaster = context.create().resource("/content/dam/fruitables/support-products/apple/jcr:content/data/master");
        Resource citrusMaster = context.create().resource("/content/dam/fruitables/support-products/citrus/jcr:content/data/master");

        ResourceResolver resourceResolver = mock(ResourceResolver.class);
        Session session = mock(Session.class);
        Workspace workspace = mock(Workspace.class);
        QueryManager queryManager = mock(QueryManager.class);
        Query query = mock(Query.class);
        QueryResult queryResult = mock(QueryResult.class);
        NodeIterator nodeIterator = mock(NodeIterator.class);
        Node appleNode = mock(Node.class);
        Node citrusNode = mock(Node.class);

        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
        when(resourceResolver.getResource(appleMaster.getPath())).thenReturn(appleMaster);
        when(resourceResolver.getResource(citrusMaster.getPath())).thenReturn(citrusMaster);
        when(session.getWorkspace()).thenReturn(workspace);
        when(workspace.getQueryManager()).thenReturn(queryManager);
        when(queryManager.createQuery(anyString(), eq(Query.JCR_SQL2))).thenReturn(query);
        when(query.execute()).thenReturn(queryResult);
        when(queryResult.getNodes()).thenReturn(nodeIterator);
        when(nodeIterator.hasNext()).thenReturn(true, true, false);
        when(nodeIterator.nextNode()).thenReturn(appleNode, citrusNode);
        when(appleNode.getPath()).thenReturn(appleMaster.getPath());
        when(citrusNode.getPath()).thenReturn(citrusMaster.getPath());

        List<Resource> masterResources = ProductCategorySupport.queryProductMasterResources(
                resourceResolver,
                "/content/dam/fruitables/support-products"
        );

        assertEquals(List.of(appleMaster, citrusMaster), masterResources);
        verify(queryManager).createQuery(anyString(), eq(Query.JCR_SQL2));
    }

    /**
     * Tests extraction, normalization, and deduplication
     * of product tags from product model.
     */
    @Test
    void testExtractProductTagIdsNormalizesAndDeduplicatesProductTags() {
        Resource resource = getResource("/content/product-with-product-tags");
        ProductCFModelTag product = resource.adaptTo(ProductCFModelTag.class);

        Set<String> tagIds = ProductCategorySupport.extractProductTagIds(
                context.resourceResolver(),
                resource.getValueMap(),
                product
        );

        assertEquals(Set.of("fruitables:apples", "fruitables:citrus"), tagIds);
    }

    /**
     * Tests fallback behavior when product tags are unavailable
     * and cq:tags are used instead.
     */
    @Test
    void testExtractProductTagIdsFallsBackToCqTags() {
        Resource resource = getResource("/content/product-with-cq-tags");

        Set<String> tagIds = ProductCategorySupport.extractProductTagIds(
                context.resourceResolver(),
                resource.getValueMap(),
                null
        );

        assertEquals(Set.of("fruitables:fresh_fruits"), tagIds);
    }

    /**
     * Tests component lookup within page hierarchy
     * and validates component ID generation.
     */
    @Test
    void testFindComponentOnPageAndBuildComponentId() {
        Resource currentComponent = getResource("/content/page/jcr:content/root/container/current");

        Resource foundComponent = ProductCategorySupport.findComponentOnPage(
                currentComponent,
                "fruitables/components/categorys-tags"
        );

        assertNotNull(foundComponent);
        assertAll(
                () -> assertEquals("/content/page/jcr:content/root/container/sidebar", foundComponent.getPath()),
                () -> assertEquals(
                        "categorys-tags-" + Math.abs(foundComponent.getPath().hashCode()),
                        ProductCategorySupport.buildComponentId(foundComponent, "categorys-tags-")
                ),
                () -> assertEquals("categorys-tags", ProductCategorySupport.buildComponentId(null, "categorys-tags-")),
                () -> assertNull(ProductCategorySupport.findComponentOnPage(
                        currentComponent,
                        "fruitables/components/does-not-exist"
                ))
        );
    }

    /**
     * Tests tag normalization, category title resolution,
     * and blank string utility check.
     */
    @Test
    void testNormalizeTagIdAndResolveCategoryTitleUseFallbackFormatting() {
        assertAll(
                () -> assertEquals(
                        "fruitables:fresh_fruits",
                        ProductCategorySupport.normalizeTagId(
                                context.resourceResolver(),
                                "/content/cq:tags/fruitables/fresh_fruits"
                        )
                ),
                () -> assertEquals(
                        "Fresh Fruits",
                        ProductCategorySupport.resolveCategoryTitle(
                                context.resourceResolver(),
                                "/content/cq:tags/fruitables/fresh_fruits"
                        )
                ),
                () -> assertTrue(ProductCategorySupport.isBlank("  "))
        );
    }

    /**
     * Retrieves resource from mock context by path.
     *
     * @param path resource path
     * @return resolved resource
     */
    private Resource getResource(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        return resource;
    }
}