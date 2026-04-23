package com.bhasaka.fruitables.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "fetch URL configuration")
public @interface HarshaApiConfig {

    @AttributeDefinition(name = "API URL")
    String fetchUrl() default "https://gorest.co.in/public/v2/users";

    @AttributeDefinition(name = "Enable Api")
    boolean enableApi() default true;
}
