package com.technoworks.TechnoBlog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.technoworks.TechnoBlog.BlogList.BlogList;

import java.util.ArrayList;

public class ActivityMain extends Activity implements ActionBar.OnNavigationListener {
    public final String ACTIVITY_LOG = "ActivityMain";
    private final String ALL_CATEG = "All", NEWS_CATEG = "Новости",
            CAROLINE_CATEG = "Caroline", CPP_CATEG = "C++";
    public boolean isDownloaded = false;
    public BlogList blogList;
    LinearLayout linLayoutList;
    LayoutInflater ltInflater;
    private int counter;
    private TextView tvLoadingState;
    private ProgressBar spinnerRing;
    private TextView.OnClickListener itemOnClick;
    private ArrayList<String> itemList;
    private ArrayList<Color> colors;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        isDownloaded = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        linLayoutList = (LinearLayout) findViewById(R.id.linLayoutList);
        ltInflater = getLayoutInflater();
        tvLoadingState = (TextView) findViewById(R.id.tvLoadingState);
        spinnerRing = (ProgressBar) findViewById(R.id.spinnerRing);

        // Set up the actionbar to show dropdown list
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        itemList = new ArrayList<String>();
        itemList.add("All");
        itemList.add("News");
        itemList.add("Caroline");
        itemList.add("C++");
        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, itemList);
        actionBar.setListNavigationCallbacks(aAdapt, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        itemOnClick = new LinearLayout.OnClickListener() {
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
                        blogList = new BlogList();
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

                                    if (blogList.getCategories(counter).toLowerCase().contains(("Новости").toLowerCase()))
                                        itemView.setBackgroundColor(Color.parseColor("#7fb5e5"));
                                    else if (blogList.getCategories(counter).toLowerCase().contains((CAROLINE_CATEG).toLowerCase()))
                                        itemView.setBackgroundColor(Color.parseColor("#7fffbb33"));
                                    else if (blogList.getCategories(counter).toLowerCase().contains((CPP_CATEG).toLowerCase()))
                                        itemView.setBackgroundColor(Color.parseColor("#7f99CC00"));


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
        }
    }

    private void changeCurrentCategory(String choosenCategory) {
        linLayoutList.removeAllViews();
        if (blogList != null)
            if (choosenCategory.equals(ALL_CATEG))
                for (counter = blogList.size() - 1; counter > 0; counter--) {
                    View itemView = ltInflater.inflate(R.layout.item, linLayoutList, false);

                    TextView tvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
                    tvSummary.setText(blogList.getSummary(counter));

                    TextView tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
                    tvCategory.setText(blogList.getCategories(counter));

                    itemView.setTag(blogList.slugList.get(counter));
                    itemView.setOnClickListener(itemOnClick);

                    if (blogList.getCategories(counter).toLowerCase().contains(("Новости").toLowerCase()))
                        itemView.setBackgroundColor(Color.parseColor("#7fb5e5"));
                    else if (blogList.getCategories(counter).toLowerCase().contains((CAROLINE_CATEG).toLowerCase()))
                        itemView.setBackgroundColor(Color.parseColor("#7fffbb33"));
                    else if (blogList.getCategories(counter).toLowerCase().contains((CPP_CATEG).toLowerCase()))
                        itemView.setBackgroundColor(Color.parseColor("#7f99CC00"));

                    linLayoutList.addView(itemView);
                }
            else {
                for (counter = blogList.size() - 1; counter > 0; counter--) {
                    if (blogList.getCategories(counter).toLowerCase().contains(choosenCategory.toLowerCase())) {
                        View itemView = ltInflater.inflate(R.layout.item, linLayoutList, false);

                        TextView tvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
                        tvSummary.setText(blogList.getSummary(counter));

                        TextView tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
                        tvCategory.setText(blogList.getCategories(counter));

                        itemView.setTag(blogList.slugList.get(counter));
                        itemView.setOnClickListener(itemOnClick);

                        if (blogList.getCategories(counter).toLowerCase().contains(("Новости").toLowerCase()))
                            itemView.setBackgroundColor(Color.parseColor("#7fb5e5"));
                        else if (blogList.getCategories(counter).toLowerCase().contains((CAROLINE_CATEG).toLowerCase()))
                            itemView.setBackgroundColor(Color.parseColor("#7fffbb33"));
                        else if (blogList.getCategories(counter).toLowerCase().contains((CPP_CATEG).toLowerCase()))
                            itemView.setBackgroundColor(Color.parseColor("#7f99CC00"));

                        linLayoutList.addView(itemView);
                    }
                }
            }
        Log.d(ACTIVITY_LOG, "category " + choosenCategory + " filtered");
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (itemPosition >= 0 && itemPosition < itemList.size() && blogList != null) {
            if (itemPosition == itemList.indexOf("News"))
                changeCurrentCategory("Новости");
            else
                changeCurrentCategory(itemList.get(itemPosition));
            return true;
        } else
            return false;
    }

    ///menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_update:
                updateList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateList() {
        isDownloaded = false;
        linLayoutList.removeAllViews();
        linLayoutList.addView(tvLoadingState);
        linLayoutList.addView(spinnerRing);
        tvLoadingState.setTextColor(Color.BLACK);
        tvLoadingState.setText("Updating posts");
        blogList = null;
        getActionBar().setSelectedNavigationItem(itemList.indexOf(ALL_CATEG));

        createList();
    }
}
