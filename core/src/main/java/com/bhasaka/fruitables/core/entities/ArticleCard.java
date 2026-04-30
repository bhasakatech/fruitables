package com.bhasaka.fruitables.core.entities;

import java.util.Date;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import lombok.Getter;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Getter
public class ArticleCard {

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String showAuthor;

    @ValueMapValue
    private String articleType;

    @ValueMapValue
    private String authorName;

    @ValueMapValue
    private Date articleDate;

    public boolean isShowAuthor() {
        return "true".equals(showAuthor);
    }
}

