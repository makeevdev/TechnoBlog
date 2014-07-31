package com.technoworks.TechnoBlog.view;

/**
 * Created by user on 31.07.2014.
 */
public class ListItem {

    private String url, tags, slug, summary;

    public ListItem(String slug, String url, String tags, String summary) {
        this.slug = slug;
        this.url = url;
        this.tags = tags;
        this.summary = summary;
    }

    public boolean compareSlug(String slugToCompare) {
        if (this.slug.equals(slugToCompare))
            return true;
        else
            return false;
    }

}
