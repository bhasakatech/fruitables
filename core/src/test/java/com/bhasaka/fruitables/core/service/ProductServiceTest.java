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
class ProductServiceTest {
    private ResourceResolver resolver;
    private QueryBuilder queryBuilder;
    private Session session;
    private Query query;
    private SearchResult searchResult;
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
    @Test
    void testNoHits() {
        when(searchResult.getHits()).thenReturn(Collections.emptyList());
        ProductCFModel result = ProductService.getProductBySlug(resolver, "any-slug");
        assertNull(result);
    }
    @Test
    void testResourceNull() throws Exception {
        Hit hit = mock(Hit.class);
        when(hit.getPath()).thenReturn("/content/dam/fruitables/products-list/item1");
        when(searchResult.getHits()).thenReturn(Arrays.asList(hit));
        when(resolver.getResource(hit.getPath())).thenReturn(null);
        ProductCFModel result = ProductService.getProductBySlug(resolver, "slug");
        assertNull(result);
    }
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
