package com.technoworks.TechnoBlog.BlogList;

import java.util.ArrayList;

/**
 * Created by Vladimir on 31.07.2014.
 */
public class ListItem {

    public String url, summary;
    public ArrayList<ItemCategory> categories;

    public ListItem(String url, ItemCategory category, String summary) {
        categories = new ArrayList<ItemCategory>();

        this.url = url;
        this.categories.add(category);
        this.summary = summary;
    }


    public void addCategory(ItemCategory categ) {
        categories.add(categ);
    }

}
