package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.adnansakel.bdl_food_app.Adapters.NewsFeedListAdapter;
import com.example.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.example.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.example.adnansakel.bdl_food_app.DataModel.PostData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Sakel on 3/29/2016.
 */
public class NewsFeedActivity extends Activity implements View.OnClickListener {
    LinearLayout llNewsFeed;
    View viewNewsFeed;
    LinearLayout llNewPost;
    LinearLayout llOrders;
    View viewNewPost;
    View viewOrders;

    ListView lvNewsFeed;

    NewsFeedListAdapter newsfeedlistadapter;

    ProgressDialog progress;

    //private List<NewsFeedData> newsFeedDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        initializeComponent();

    }
    /*
    public void sendMessage(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, PayPal_selection.class);
        startActivity(intent);
    }*/
    private void initializeComponent(){
        llNewsFeed = (LinearLayout)findViewById(R.id.linear_layout_news_feed);
        llNewPost = (LinearLayout)findViewById(R.id.linear_layout_new_post);
        llOrders = (LinearLayout)findViewById(R.id.llOrder);
        viewNewsFeed = (View)findViewById(R.id.view_news_feed);
        viewNewPost = (View)findViewById(R.id.view_new_post);
        viewOrders = (View)findViewById(R.id.viewOrders);


        lvNewsFeed = (ListView)findViewById(R.id.lvNewsfeed);

        llNewPost.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewPost.setBackgroundResource(R.drawable.add_new_black);

        llOrders.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewOrders.setBackgroundResource(R.drawable.cart_black);

        llNewsFeed.setBackgroundColor(Color.parseColor("#33ffffff"));
        viewNewsFeed.setBackgroundResource(R.drawable.home_white);


        viewNewPost.setOnClickListener(this);
        //llOrders.setOnClickListener(this);
        viewOrders.setOnClickListener(this);

        newsfeedlistadapter = new NewsFeedListAdapter(this);
        lvNewsFeed.setAdapter(newsfeedlistadapter);

        lvNewsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeedData newsFeedData = new NewsFeedData();
                newsFeedData = (NewsFeedData)newsfeedlistadapter.getItem(position);
                startActivity(new Intent(NewsFeedActivity.this,PostDetailsActivity.class).putExtra(AppConstants.POST_DETAILS,newsFeedData));
                NewsFeedActivity.this.finish();
            }
        });

        progress = ProgressDialog.show(this, null,
                null, true);
        progress.setContentView(R.layout.progressdialogview);
        progress.setCancelable(true);

        loadNewsFeed();

    }

    private void loadNewsFeed(){
        //newsFeedDataList = new ArrayList<NewsFeedData>();
        Firebase.setAndroidContext(this);
        Firebase newsfeedRef = new Firebase("https://bdlfoodapp.firebaseio.com/Posts");

        progress.show();

        Query queryRef = newsfeedRef.orderByKey().limitToLast(50);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //System.out.println("Hello");
                //System.out.println(dataSnapshot.toString());
                //for(DataSnapshot post : dataSnapshot.getChildren()){
                    NewsFeedData nfd = new NewsFeedData();
                   // PostData pd = dataSnapshot.getValue(PostData.class); not working; don't know why
                nfd.setDishName(dataSnapshot.child("DishName").getValue().toString());
                nfd.setLocation(dataSnapshot.child("Location").getValue().toString());
                nfd.setNumberofDishes(dataSnapshot.child("NumberofDishes").getValue().toString());
                nfd.setOrderBefore(dataSnapshot.child("OrderBefore").getValue().toString());
                nfd.setPrice("50");//for the time being some dummy price
                nfd.setPostMessage(dataSnapshot.child("PostMessage").getValue().toString());
                nfd.setIngredients(dataSnapshot.child("Ingredients").getValue().toString());
                nfd.setPostKey(dataSnapshot.getKey().toString());
                nfd.setPostOwner_UserID(dataSnapshot.child("UserID").getValue().toString());
                nfd.setPostOwner_FirebaseKey(dataSnapshot.child("FirebaseUserKey").getValue().toString());
                newsfeedlistadapter.addItem(nfd);
                progress.dismiss();
                //System.out.println(dataSnapshot.getKey().toString());
                   // System.out.println("Posts :"+post.getKey()+"\n"+post.getValue().toString());

               // }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    System.out.println("Came here 1");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("Came here 2");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                System.out.println("Came here 3");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Came here 4");
                progress.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        System.out.println("Clicked");
        if(v == viewNewPost){
            System.out.println("Clicked newPost");
            startActivity(new Intent(NewsFeedActivity.this, NewPostActivity.class));
            this.finish();
        }
        if(v == viewOrders){
            System.out.println("Clicked Orders");
            startActivity(new Intent(NewsFeedActivity.this, OrderListActivity.class));
            this.finish();
        }
    }


}
