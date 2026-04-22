package com.bhasaka.fruitables.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface DynamicAPIConfig {

    @AttributeDefinition
    String api() default "https://dummyjson.com/products";

    @AttributeDefinition
    boolean status() default true;
}
