package com.bhasaka.fruitables.core.models;

import com.bhasaka.fruitables.core.entities.ArticleCard;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Getter
public class DynamicArticlesModel {

    @ChildResource
    private List<ArticleCard> articles;
}