package com.bhasaka.fruitables.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TopHeaderModel {

    @ValueMapValue
    private String address;

    @ValueMapValue
    private String email;

    @ChildResource(name = "policyLinks")
    private List<PolicyLink> policyLinks;

    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public List<PolicyLink> getPolicyLinks() { return policyLinks; }
}
