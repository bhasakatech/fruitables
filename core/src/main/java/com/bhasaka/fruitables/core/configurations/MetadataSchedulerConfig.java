package com.bhasaka.fruitables.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "XF Metadata Scheduler Config"
)
public @interface MetadataSchedulerConfig {

    @AttributeDefinition(
            name = "Cron Expression"
    )
    String scheduler_expression() default "0 0 0 * * ?";

    @AttributeDefinition(
            name = "Enable Scheduler"
    )
    boolean scheduler_enabled() default true;
}