package com.bhasaka.fruitables.core.models;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.bhasaka.fruitables.core.entities.CFArticle;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DynamicCFArticleModel {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicCFArticleModel.class);
    private List<CFArticle> cfDataList = new ArrayList<>();

    @ValueMapValue
    private String[] cfPath;

    @ValueMapValue
    private String sourceType;

    @SlingObject
    private ResourceResolver resourceResolver;

    //Getters
    public String[] getCfPath() {
        return cfPath;
    }

    public List<CFArticle> getCfDataList() {
        return cfDataList;
    }

    public String getSourceType() {
        return sourceType;
    }

    @PostConstruct
    protected void init() {
        if(cfPath != null) {
            for(String contentFragmentPath : cfPath) {
                LOG.info("Cf Path : {}", contentFragmentPath);
                Resource contentFragmentresource = resourceResolver.getResource(contentFragmentPath);
                if(contentFragmentresource != null) {
                    ContentFragment contentFragment = contentFragmentresource.adaptTo(ContentFragment.class);

                    if(contentFragment != null) {
                        CFArticle dynamicCfArticles = new CFArticle();
                        dynamicCfArticles.setArticleImage(contentFragment.getElement("articleImage").getContent());
                        dynamicCfArticles.setDescription(contentFragment.getElement("description").getContent());
                        dynamicCfArticles.setButtonLabel(contentFragment.getElement("buttonLabel").getContent());
                        dynamicCfArticles.setButtonLink(contentFragment.getElement("buttonLink").getContent());
                        dynamicCfArticles.setArticleType(contentFragment.getElement("articleType").getContent());
                        dynamicCfArticles.setAuthorName(contentFragment.getElement("authorName").getContent());
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = simpleDateFormat.parse(contentFragment.getElement("articleDate").getContent());
                            dynamicCfArticles.setArticleDate(date);

                        } catch (Exception e) {
                            LOG.error("Getting exception : {}", e.getMessage());
                        }
                        cfDataList.add(dynamicCfArticles);
                    }
                }
            }
        } else {
            LOG.error("Content Fragment Path is empty");
        }
    }

}