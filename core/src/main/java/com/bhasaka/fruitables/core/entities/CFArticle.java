package com.bhasaka.fruitables.core.entities;

import java.util.Date;

public class CFArticle {
    private String articleImage;
    private String description;
    private String buttonLabel;
    private String buttonLink;
    private boolean showMoreDetails;
    private String articleType;
    private String authorName;
    private Date articleDate;

    public CFArticle() {
        super();
    }

    public CFArticle(String articleImage, String description, String buttonLabel, String buttonLink, boolean showMoreDetails, String articleType,
                                String authorName, Date articleDate) {
        super();
        this.articleImage = articleImage;
        this.description = description;
        this.buttonLabel = buttonLabel;
        this.buttonLink = buttonLink;
        this.showMoreDetails = showMoreDetails;
        this.articleType = articleType;
        this.authorName = authorName;
        this.articleDate = articleDate;
    }

    //Setters
    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public void setButtonLink(String buttonLink) {
        this.buttonLink = buttonLink;
    }

    public void setShowMoreDetails(boolean showMoreDetails) {
        this.showMoreDetails = showMoreDetails;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setArticleDate(Date articleDate) {
        this.articleDate = articleDate;
    }

    //Getters
    public String getArticleImage() {
        return articleImage;
    }

    public String getDescription() {
        return description;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public String getButtonLink() {
        return buttonLink;
    }

    public boolean isShowMoreDetails() {
        return showMoreDetails;
    }

    public String getArticleType() {
        return articleType;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Date getArticleDate() {
        return articleDate;
    }
}
