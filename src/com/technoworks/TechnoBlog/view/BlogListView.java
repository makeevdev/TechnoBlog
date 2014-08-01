package com.technoworks.TechnoBlog.view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    public ArrayList<String> slugList;
    HashMap<String, ListItem> mapOfPosts;
    JSONArray jsonArrayPosts;


    public BlogListView() {
        mapOfPosts = new HashMap<String, ListItem>();
        slugList = new ArrayList<String>();
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
                    // 4. ad key(slug) to arraylist
                    slugList.add(obj.getString(TAG_SLUG));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public int size() {
        return slugList.size();
    }

    public String getSummary(int i) {
        return mapOfPosts.get(slugList.get(i)).summary;
    }

    public String getCategories(int numberOfitem) {
        String out = "Categories: ";
        int countOfCategories = mapOfPosts.get(slugList.get(numberOfitem)).categories.size();
        if (countOfCategories < 2)
            return "Categories: " + mapOfPosts.get(slugList.get(numberOfitem)).categories.get(0).title;
        else {
            for (int i = 0; i < numberOfitem; i++)
                if (i == numberOfitem - 1)
                    out += mapOfPosts.get(slugList.get(numberOfitem)).categories.get(0).title + ", ";
                else
                    out += mapOfPosts.get(slugList.get(numberOfitem)).categories.get(0).title;
        }
        return out;
    }

    public String getUrl(int i) {
        return mapOfPosts.get(slugList.get(i)).url;
    }
}
