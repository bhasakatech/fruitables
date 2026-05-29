package com.bhasaka.fruitables.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Custom Sitemap Configuration",
        description = "Configuration for Sitemap Generation"
)
public @interface SitemapConfig {

    @AttributeDefinition(
            name = "Cron Expression",
            description = "Scheduler Cron Expression"
    )
    String scheduler_expression() default "0/30 * * * * ?";

    @AttributeDefinition(
            name = "Root Path",
            description = "Website Root Path"
    )
    String root_path() default "/content/fruitables/us/en/pages";

    @AttributeDefinition(
            name = "DAM Folder Path",
            description = "DAM Folder Path"
    )
    String dam_folder_path() default "/content/dam/fruitables";
}