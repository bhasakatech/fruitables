package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link ProductCategorySupport}.
 */
@ExtendWith(AemContextExtension.class)
class ProductCategorySupportTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductCFModelTag.class);
        context.load().json("/productCategorySupportTest.json", "/content");

        mockTagManager(); 
    }

    /**
     * Mock TagManager and Tag behavior
     */
     private void mockTagManager() {

        TagManager tagManager = mock(TagManager.class);

        Tag apple = mock(Tag.class);
        Tag citrus = mock(Tag.class);
        Tag fresh = mock(Tag.class);

        // Correct way to mock adaptTo
        context.registerAdapter(ResourceResolver.class, TagManager.class, tagManager);

        when(tagManager.resolve("/content/cq:tags/fruitables/apples")).thenReturn(apple);
        when(tagManager.resolve("/content/cq:tags/fruitables/citrus")).thenReturn(citrus);
        when(tagManager.resolve("/content/cq:tags/fruitables/fresh_fruits")).thenReturn(fresh);

        when(apple.getTagID()).thenReturn("fruitables:apples");
        when(citrus.getTagID()).thenReturn("fruitables:citrus");
        when(fresh.getTagID()).thenReturn("fruitables:fresh_fruits");

        when(fresh.getTitle()).thenReturn("Fresh Fruits");
    }

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
                () -> assertEquals(
                        "categorys-tags-",
                        ProductCategorySupport.buildComponentId(null, "categorys-tags-")
                ),
                () -> assertNull(
                        ProductCategorySupport.findComponentOnPage(
                                currentComponent,
                                "fruitables/components/does-not-exist"
                        )
                )
        );
    }

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

    private Resource getResource(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        return resource;
    }
}