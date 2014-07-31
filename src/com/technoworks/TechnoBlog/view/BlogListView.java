package com.technoworks.TechnoBlog.view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by user on 31.07.2014.
 */


public class BlogListView {

    private final String TAG_URL = "url";
    private final String TAG_SLUG = "slug";
    private final String TAG_SUMMARY = "summary";
    private final String TAG_TITLE = "title";
    private final String TAG_CATEGORIES = "categories";

    HashMap<String, ListItem> mapOfPosts;
    JSONArray jsonArrayPosts;


    public BlogListView() {
        mapOfPosts = new HashMap<String, ListItem>();

    }


    public void setPostsData(String inputStr) {

        if (inputStr != null) {
            try {

                // 1. convert string to JSONArray
                jsonArrayPosts = new JSONArray(inputStr); // it's already an array
                // 2. parse json to List
                for (int i = 0; i < jsonArrayPosts.length(); i++) {
                    JSONObject obj = jsonArrayPosts.getJSONObject(i);
                    ListItem item = null; // new item for each object in JSONArray

                    // category node is JSON  object
                    JSONObject objCategories = obj.getJSONObject(TAG_CATEGORIES);
                    for (Iterator key = objCategories.keys(); key.hasNext(); ) { // foreach keys in categories
                        JSONObject objCategory = objCategories.getJSONObject((String) key.next()); // get categorY

                        String categUrl = objCategory.getString(TAG_URL);
                        String categTitle = objCategory.getString(TAG_TITLE);

                        ItemCategory category = new ItemCategory(categUrl, categTitle);
                        if (item == null)
                            item = new ListItem(obj.getString(TAG_URL), category, obj.getString(TAG_SUMMARY));
                        else
                            item.addCategory(category);
                    }

                    // 3. set data from List do ListView
                    mapOfPosts.put(obj.getString(TAG_SLUG), item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
