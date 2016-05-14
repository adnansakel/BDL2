package com.foogle.adnansakel.bdl_food_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.foogle.adnansakel.bdl_food_app.Adapters.NewsFeedListAdapter;
import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.foogle.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by Adnan Sakel on 5/4/2016.
 */
public class NewsFeedActivityS {
    View view;
    Context contex;
    LinearLayout llNewsFeed;
    View viewNewsFeed;
    LinearLayout llNewPost;
    LinearLayout llOrders;
    LinearLayout llSettingsMenu;
    View viewNewPost;
    View viewOrders;

    ListView lvNewsFeed;

    NewsFeedListAdapter newsfeedlistadapter;

    ProgressDialog progress;

    Integer newsFeedSortRule = 0;
    Spinner spSortRule;
    String[] arrSpSortRule = {"All", "Buy Requests", "Sell Requests", "Post Time", "Price", "Servings", "Location"};

    public NewsFeedActivityS(View view, Context context){
        this.view = view;
        this.contex = context;
        initializeComponent();
    }

    private void initializeComponent(){
        /*
        llNewsFeed = (LinearLayout)view.findViewById(R.id.linear_layout_news_feed);
        llNewPost = (LinearLayout)view.findViewById(R.id.linear_layout_new_post);
        llOrders = (LinearLayout)view.findViewById(R.id.llOrder);
        llSettingsMenu = (LinearLayout)view.findViewById(R.id.linear_layout_settings);
        viewNewsFeed = (View)view.findViewById(R.id.view_news_feed);
        viewNewPost = (View)view.findViewById(R.id.view_new_post);
        viewOrders = (View)view.findViewById(R.id.viewOrders);


        lvNewsFeed = (ListView)view.findViewById(R.id.lvNewsfeed);

        llNewPost.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewPost.setBackgroundResource(R.drawable.add_new_black);

        llOrders.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewOrders.setBackgroundResource(R.drawable.cart_black);

        llNewsFeed.setBackgroundColor(Color.parseColor("#33ffffff"));
        viewNewsFeed.setBackgroundResource(R.drawable.home_white);
*/
        view.findViewById(R.id.llheader).setVisibility(View.INVISIBLE);
        //viewNewPost.setOnClickListener(contex);
        //llOrders.setOnClickListener(this);
        //viewOrders.setOnClickListener(this);

        //llSettingsMenu.setOnClickListener(this);

        newsfeedlistadapter = new NewsFeedListAdapter(contex);
        lvNewsFeed.setAdapter(newsfeedlistadapter);

        lvNewsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeedData newsFeedData = new NewsFeedData();
                newsFeedData = (NewsFeedData) newsfeedlistadapter.getItem(position);
                contex.startActivity(new Intent(contex, PostDetailsActivity.class).putExtra(AppConstants.POST_DETAILS, newsFeedData));
                //contex.finish();
            }
        });

        progress = ProgressDialog.show(contex, null,
                null, true);
        progress.setContentView(R.layout.progressdialogview);
        progress.setCancelable(true);

        //loadNewsFeed();

        spSortRule = (Spinner)view.findViewById(R.id.spinner_sort_newsfeed);
        ArrayAdapter<String> spSrAdapter = new ArrayAdapter<String>(contex,
                android.R.layout.simple_spinner_item, arrSpSortRule);
        spSortRule.setAdapter(spSrAdapter);
        spSortRule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Object item = arg0.getItemAtPosition(arg2);
                if (item!=null) {
                    Toast.makeText(contex, item.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(contex, "Selected",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void loadNewsFeed(){
        //newsFeedDataList = new ArrayList<NewsFeedData>();
        Firebase.setAndroidContext(contex);
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


                String Image_String = (String) dataSnapshot.child("Image").getValue();
                if(Image_String != null){
                    nfd.setImage(Image_String);}
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


}
