package com.bhasaka.fruitables.core.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Getter
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Slf4j
public class SeoModel {

    @ValueMapValue
    private String ogTitle;

    @ValueMapValue
    private String ogDescription;

    @ValueMapValue
    private String ogImage;

    @PostConstruct
    public void init(){
        log.info("OG Title {}",ogTitle);
        log.info("OG Description {}",ogDescription);
        log.info("OG Image {}",ogImage);
    }
}