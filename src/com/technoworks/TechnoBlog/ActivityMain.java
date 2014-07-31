package com.technoworks.TechnoBlog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

        final LinearLayout linLayoutList = (LinearLayout) findViewById(R.id.linLayoutList);
        final LayoutInflater ltInflater = getLayoutInflater();

        Thread network = new Thread() { // поток тянет данные с сервера, формирует лист с данными
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
        };
        network.start();

        try {
            network.join();
        } catch (InterruptedException e) {
        }


        for (int i = 0; i < blogList.size(); i++) {
            View itemView = ltInflater.inflate(R.layout.item, linLayoutList, false);
            TextView tvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
            tvSummary.setText(blogList.getSummary(i));
            TextView tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvCategory.setText(blogList.getCategories(i));
            linLayoutList.addView(itemView);
        }


    }
}
