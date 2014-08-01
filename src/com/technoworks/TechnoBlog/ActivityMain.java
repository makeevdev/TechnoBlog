package com.technoworks.TechnoBlog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.technoworks.TechnoBlog.view.BlogListView;

public class ActivityMain extends Activity {
    public final String ACTIVITY_LOG = "ActivityMain";
    public boolean isDownloaded = false;
    public BlogListView blogList;
    LinearLayout linLayoutList;
    LayoutInflater ltInflater;
    private int counter;
    private TextView tvLoadingState;
    private ProgressBar spinnerRing;
    private TextView.OnClickListener itemOnClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isDownloaded = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        linLayoutList = (LinearLayout) findViewById(R.id.linLayoutList);
        ltInflater = getLayoutInflater();
        tvLoadingState = (TextView) findViewById(R.id.tvLoadingState);
        spinnerRing = (ProgressBar) findViewById(R.id.spinnerRing);
    }

    @Override
    public void onResume() {
        super.onResume();

        itemOnClick = new TextView.OnClickListener() {
            public void onClick(View v) {
                int i = blogList.slugList.indexOf(v.getTag());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://technoworks.ru"
                        + blogList.getUrl(i)));
                startActivity(browserIntent);
            }
        };

        createList();
    }

    private void createList() {
        if (!isDownloaded) {
            Thread network = new Thread() { // поток тянет данные с сервера, формирует лист с данными
                @Override
                public void run() {
                    String out = DataSender.getStringFromHTTP("/blag/api/v1.0/posts/");
                    if (out != null) {
                        blogList = new BlogListView();
                        blogList.setPostsData(out);
                        Log.d(ACTIVITY_LOG, "Creating BlogList completed");
                        isDownloaded = true;

                        linLayoutList.post(new Runnable() {
                            @Override
                            public void run() { // влезаем в главный поток
                                linLayoutList.removeView(spinnerRing);
                                linLayoutList.removeView(tvLoadingState);

                                for (counter = blogList.size() - 1; counter > 0; counter--) {
                                    View itemView = ltInflater.inflate(R.layout.item, linLayoutList, false);

                                    TextView tvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
                                    tvSummary.setText(blogList.getSummary(counter));

                                    TextView tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
                                    tvCategory.setText(blogList.getCategories(counter));

                                    itemView.setTag(blogList.slugList.get(counter));
                                    itemView.setOnClickListener(itemOnClick);
                                    linLayoutList.addView(itemView);
                                }
                                Log.d(ACTIVITY_LOG, "Drawing completed");
                            }
                        });

                    } else
                        Log.d(ACTIVITY_LOG, "GET request failed");
                    tvLoadingState.post(new Runnable() {
                        @Override
                        public void run() {
                            linLayoutList.removeView(spinnerRing);
                            tvLoadingState.setTextColor(Color.RED);
                            tvLoadingState.setText("Loading failed");
                        }
                    });
                }
            };
            network.start();

            /*
            try {
                network.join();
            } catch (InterruptedException e) {
            }
            */

            /*
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
        */
        }
    }

}
