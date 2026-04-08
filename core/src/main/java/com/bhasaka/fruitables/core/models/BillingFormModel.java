package com.bhasaka.fruitables.core.models;

import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Getter
public class BillingFormModel {

    @ValueMapValue
    private String sectionTitle;

    @ValueMapValue
    private String firstNameLabel;

    @ValueMapValue
    private String lastNameLabel;

    @ValueMapValue
    private String companyNameLabel;

    @ValueMapValue
    private String addressLabel;

    @ValueMapValue
    private String addressPlaceholder;

    @ValueMapValue
    private String cityLabel;

    @ValueMapValue
    private String countryLabel;

    @ValueMapValue
    private String zipLabel;

    @ValueMapValue
    private String mobileLabel;

    @ValueMapValue
    private String emailLabel;

    @ValueMapValue
    private String createAccountLabel;

    @ValueMapValue
    private String shipDifferentAddressLabel;

    @ValueMapValue
    private String notesPlaceholder;

    @PostConstruct
    protected void init() {
    }
}