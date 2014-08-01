package com.technoworks.TechnoBlog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.technoworks.TechnoBlog.view.BlogListView;

public class ActivityMain extends Activity {
    public final String ACTIVITY_LOG = "ActivityMain";
    public boolean isDownloaded = false;
    public BlogListView blogList;
    LinearLayout linLayoutList;
    LayoutInflater ltInflater;
    private TextView tvLoadingState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isDownloaded = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        linLayoutList = (LinearLayout) findViewById(R.id.linLayoutList);
        ltInflater = getLayoutInflater();
        tvLoadingState = (TextView) findViewById(R.id.tvLoadingState);
        tvLoadingState.invalidate();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isDownloaded) {
            Thread network = new Thread() { // поток тянет данные с сервера, формирует лист с данными
                @Override
                public void run() {
                    String out = DataSender.getStringFromHTTP("/blag/api/v1.0/posts/");
                    if (out != null) {
                        blogList = new BlogListView();
                        blogList.setPostsData(out);
                        Log.d(ACTIVITY_LOG, "GET completed");
                        isDownloaded = true;

                    } else
                        Log.d(ACTIVITY_LOG, "GET failed");
                }
            };
            network.start();


            try {
                network.join();
            } catch (InterruptedException e) {
            }


            if (isDownloaded) {

                for (int i = blogList.size() - 1; i > 0; i--) {
                    View itemView = ltInflater.inflate(R.layout.item, linLayoutList, false);
                    TextView tvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
                    tvSummary.setText(blogList.getSummary(i));
                    TextView tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
                    tvCategory.setText(blogList.getCategories(i));
                    linLayoutList.addView(itemView);
                    linLayoutList.removeView(tvLoadingState);
                }
            } else {
                tvLoadingState.setTextColor(Color.RED);
                tvLoadingState.setText("Loading failed");
            }
        }
    }

}
