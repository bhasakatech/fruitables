package com.bhasaka.fruitables.core.service;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component(service = ServiceUtil.class, immediate = true)
public class ServiceUtil {

    @Reference
    ResourceResolverFactory resolverFactory;

    public ResourceResolver getServiceUserMap() throws LoginException {
        ResourceResolver resolver = null;
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "pagecleanupservice");
        try{
            resolver = resolverFactory.getServiceResourceResolver(map);
        }
        catch(LoginException e){
            e.printStackTrace();
        }
        return resolver;
    }
}