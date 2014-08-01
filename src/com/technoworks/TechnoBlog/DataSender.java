package com.technoworks.TechnoBlog;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class DataSender {
    static final String mServerUrl = "http://technoworks.ru";
    static final String SENDER_TAG = "DataSender";

    public static String getStringFromHTTP(String api) {
        HttpClient http = new DefaultHttpClient();
        String output = null;

        try {
            Log.d(SENDER_TAG, "Loading started");
            HttpGet httpGet = new HttpGet(mServerUrl + api);
            HttpResponse httpResponse = http.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            output = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            Log.d(SENDER_TAG, "Loading completed");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output; // if not created, will return null
    }

    public static String PostJSONandGetString(String api, JSONObject jsonObject) {
        String result = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            StringEntity se = new StringEntity(jsonObject.toString());

            HttpPost httpPost = new HttpPost(mServerUrl + api);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Content-Type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}

