package com.bhasaka.fruitables.core.service;

import com.bhasaka.fruitables.core.models.ProductCFModel;
import com.day.cq.search.*;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.jcr.Session;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link ProductService}.
 * <p>
 * This class tests the behavior of the method
 * {@code getProductBySlug(ResourceResolver, String)} under different scenarios
 * using Mockito for mocking dependencies.
 * </p>
 */
class ProductServiceTest {

    /**
     * Mocked ResourceResolver used for adapting to QueryBuilder and Session.
     */
    private ResourceResolver resolver;

    /**
     * Mocked QueryBuilder for creating queries.
     */
    private QueryBuilder queryBuilder;

    /**
     * Mocked JCR Session.
     */
    private Session session;

    /**
     * Mocked Query object.
     */
    private Query query;

    /**
     * Mocked SearchResult containing query results.
     */
    private SearchResult searchResult;

    /**
     * Initializes mocks and common behavior before each test.
     */
    @BeforeEach
    void setup() {
        resolver = mock(ResourceResolver.class);
        queryBuilder = mock(QueryBuilder.class);
        session = mock(Session.class);
        query = mock(Query.class);
        searchResult = mock(SearchResult.class);

        when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
    }

    /**
     * Tests the scenario when no search results (hits) are returned.
     * Expected result: null.
     */
    @Test
    void testNoHits() {
        when(searchResult.getHits()).thenReturn(Collections.emptyList());

        ProductCFModel result = ProductService.getProductBySlug(resolver, "any-slug");

        assertNull(result);
    }

    /**
     * Tests the scenario where the resource is null for a given hit.
     * Expected result: null.
     *
     * @throws Exception if any mock interaction fails
     */
    @Test
    void testResourceNull() throws Exception {
        Hit hit = mock(Hit.class);
        when(hit.getPath()).thenReturn("/content/dam/fruitables/products-list/item1");

        when(searchResult.getHits()).thenReturn(Arrays.asList(hit));
        when(resolver.getResource(hit.getPath())).thenReturn(null);

        ProductCFModel result = ProductService.getProductBySlug(resolver, "slug");

        assertNull(result);
    }

    /**
     * Tests the scenario where the master node is null.
     * Expected result: null.
     *
     * @throws Exception if any mock interaction fails
     */
    @Test
    void testMasterNull() throws Exception {
        Hit hit = mock(Hit.class);
        when(hit.getPath()).thenReturn("/content/dam/fruitables/products-list/item2");

        Resource resource = mock(Resource.class);

        when(resolver.getResource(hit.getPath())).thenReturn(resource);
        when(resolver.getResource(resource.getPath() + "/jcr:content/data/master")).thenReturn(null);
        when(searchResult.getHits()).thenReturn(Arrays.asList(hit));

        ProductCFModel result = ProductService.getProductBySlug(resolver, "slug");

        assertNull(result);
    }

    /**
     * Tests the scenario where the Content Fragment model adaptation returns null.
     * Expected result: null.
     *
     * @throws Exception if any mock interaction fails
     */
    @Test
    void testCFNull() throws Exception {
        Hit hit = mock(Hit.class);
        when(hit.getPath()).thenReturn("/content/dam/fruitables/products-list/item3");

        Resource resource = mock(Resource.class);
        Resource master = mock(Resource.class);

        when(resolver.getResource(hit.getPath())).thenReturn(resource);
        when(resource.getPath()).thenReturn("/content/dam/fruitables/products-list/item3");
        when(resolver.getResource(resource.getPath() + "/jcr:content/data/master")).thenReturn(master);
        when(master.adaptTo(ProductCFModel.class)).thenReturn(null);
        when(searchResult.getHits()).thenReturn(Arrays.asList(hit));

        ProductCFModel result = ProductService.getProductBySlug(resolver, "slug");

        assertNull(result);
    }

    /**
     * Tests the scenario where the generated slug matches the input slug.
     * Expected result: non-null ProductCFModel.
     *
     * @throws Exception if any mock interaction fails
     */
    @Test
    void testSlugMatch() throws Exception {
        Hit hit = mock(Hit.class);
        when(hit.getPath()).thenReturn("/content/dam/fruitables/products-list/item4");
        Resource resource = mock(Resource.class);
        Resource master = mock(Resource.class);
        ProductCFModel cf = mock(ProductCFModel.class);
        when(resolver.getResource(hit.getPath())).thenReturn(resource);
        when(resource.getPath()).thenReturn("/content/dam/fruitables/products-list/item4");
        when(resolver.getResource(resource.getPath() + "/jcr:content/data/master")).thenReturn(master);
        when(master.adaptTo(ProductCFModel.class)).thenReturn(cf);
        when(cf.getProductName()).thenReturn("Test Product");
        when(searchResult.getHits()).thenReturn(Arrays.asList(hit));

        ProductCFModel result = ProductService.getProductBySlug(resolver, "test-product");

        assertNotNull(result);
        assertEquals(cf, result);
    }

    /**
     * Tests the scenario where the generated slug does not match the input slug.
     * Expected result: null.
     *
     * @throws Exception if any mock interaction fails
     */
    @Test
    void testSlugMismatch() throws Exception {
        Hit hit = mock(Hit.class);
        when(hit.getPath()).thenReturn("/content/dam/fruitables/products-list/item5");

        Resource resource = mock(Resource.class);
        Resource master = mock(Resource.class);
        ProductCFModel cf = mock(ProductCFModel.class);

        when(resolver.getResource(hit.getPath())).thenReturn(resource);
        when(resource.getPath()).thenReturn("/content/dam/fruitables/products-list/item5");
        when(resolver.getResource(resource.getPath() + "/jcr:content/data/master")).thenReturn(master);
        when(master.adaptTo(ProductCFModel.class)).thenReturn(cf);
        when(cf.getProductName()).thenReturn("Another Product");
        when(searchResult.getHits()).thenReturn(Arrays.asList(hit));
        ProductCFModel result = ProductService.getProductBySlug(resolver, "test-product");
        assertNull(result);
    }
}