package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class SettingsMenuActivity extends Activity implements AdapterView.OnItemClickListener,
        View.OnClickListener{


    ListView lvSettingsMenu;

    LinearLayout llSettings;
    View viewSettings;
    LinearLayout llNewsFeed;
    View viewNewsFeed;
    LinearLayout llNewPost;
    View viewNewPost;
    LinearLayout llOrders;
    View viewOrders;

    String menu_list_items[] = {"Profile", "Options", "Logout", "About"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
        initializeComponent();
    }

    private void initializeComponent(){
        lvSettingsMenu = (ListView) findViewById(R.id.list_view_menu_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu_list_items);
        lvSettingsMenu.setAdapter(adapter);
        lvSettingsMenu.setOnItemClickListener(this);


        llNewsFeed = (LinearLayout)findViewById(R.id.linear_layout_news_feed);
        llNewPost = (LinearLayout)findViewById(R.id.linear_layout_new_post);
        llSettings = (LinearLayout)findViewById(R.id.linear_layout_settings);
        llOrders = (LinearLayout)findViewById(R.id.llOrder);

        viewOrders = (View)findViewById(R.id.viewOrders);
        viewNewsFeed = (View)findViewById(R.id.view_news_feed);
        viewNewPost = (View)findViewById(R.id.view_new_post);
        viewSettings = (View)findViewById(R.id.view_settings);

        llNewPost.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewPost.setBackgroundResource(R.drawable.add_new_black);

        llNewsFeed.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewsFeed.setBackgroundResource(R.drawable.home_black);

        llOrders.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewOrders.setBackgroundResource(R.drawable.cart_black);

        llSettings.setBackgroundColor(Color.parseColor("#33ffffff"));
        viewSettings.setBackgroundResource(R.drawable.settingswhite);

        viewNewPost.setOnClickListener(this);
        viewNewsFeed.setOnClickListener(this);
        viewOrders.setOnClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView parent, View v, int position, long id) {
        if(position == 0)
        {
            startActivity(new Intent(SettingsMenuActivity.this, UserProfileActivity.class));
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == viewNewPost){
            startActivity(new Intent(SettingsMenuActivity.this, NewPostActivity.class));
            this.finish();
        }
        if (v == viewNewsFeed)
        {
            startActivity(new Intent(SettingsMenuActivity.this, NewsFeedActivity.class));
            this.finish();
        }
        if (v == viewOrders)
        {
            startActivity(new Intent(SettingsMenuActivity.this, OrderListActivity.class));
            this.finish();
        }
    }

}