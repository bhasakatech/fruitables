package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

/**
 * Sling Model for Top Header component.
 *
 * <p>This model adapts from {@link Resource} and provides
 * header details such as address, email, and policy links.</p>
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TopHeaderModel {

    @ValueMapValue
    private String address;

    @ValueMapValue
    private String email;

    @ChildResource(name = "policyLinks")
    private List<PolicyLink> policyLinks;

    /**
     * Returns the address displayed in the header.
     *
     * @return address text
     */
    public String getAddress() { return address; }

    /**
     * Returns the email displayed in the header.
     *
     * @return email address
     */
    public String getEmail() { return email; }

    /**
     * Returns the list of policy links.
     *
     * @return list of {@link PolicyLink}
     */
    public List<PolicyLink> getPolicyLinks() { return policyLinks; }
}