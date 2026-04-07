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
    private String placeholder;

    @ValueMapValue
    private String buttonLabel;

    @PostConstruct
    protected void init() {
    }
}