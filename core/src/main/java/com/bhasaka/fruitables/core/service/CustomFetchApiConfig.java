package com.bhasaka.fruitables.core.service;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "API Configuration")
public @interface CustomFetchApiConfig {

    @AttributeDefinition(name = "API URL")
    String apiUrl();
    @AttributeDefinition(name = "Enable API")
    boolean apiEnabled();
}
