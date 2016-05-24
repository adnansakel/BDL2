package com.foogle.adnansakel.bdl_food_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.foogle.adnansakel.bdl_food_app.Adapters.ViewPagerAdapter;
import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.foogle.adnansakel.bdl_food_app.Helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"News Feed","Orders","New Post"};
    int Numboftabs =3;
    View newsFeedView;
    View orderListView;
    View newPostView;
    Button btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        btnSettings = (Button)findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsMenuActivity.class));
            }
        });

//        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        newsFeedView =  pager.findViewById(R.id.rlNewsFeed);
        orderListView = pager.findViewById(R.id.llorderList);
        newPostView = pager.findViewById(R.id.llnewPost);
        //new NewsFeedActivityS(newsFeedView, this);
        //adapter.getItem(0).getView().findViewById(R.id.llheader).setVisibility(View.INVISIBLE);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.parseColor("#dddddd");
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(this);
//        newsFeedView.findViewById(R.id.llheader).setVisibility(View.INVISIBLE);
//        pager.getFocusedChild().findViewById(R.id.llheader).setVisibility(View.VISIBLE);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       // Toast.makeText(this,position+" Selected",Toast.LENGTH_LONG).show();
        if(position == 1 && AppConstants.IsExploring){

                Toast.makeText(this, "You must login to view your orders", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
