package com.technoworks.TechnoBlog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.technoworks.TechnoBlog.view.BlogListView;

public class ActivityMain extends Activity {
    public final String ACTIVITY_LOG = "ActivityMain";
    /**
     * Called when the activity is first created.
     */

    public BlogListView blogList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        (new Thread() {
            @Override
            public void run() {
                String out = DataSender.getStringFromHTTP("/blag/api/v1.0/posts/");
                if (out != null) {
                    blogList = new BlogListView();
                    blogList.setPostsData(out);
                    Log.d(ACTIVITY_LOG, "GET completed");
                } else
                    Log.d(ACTIVITY_LOG, "GET failed");
            }
        }).start();


    }
}
