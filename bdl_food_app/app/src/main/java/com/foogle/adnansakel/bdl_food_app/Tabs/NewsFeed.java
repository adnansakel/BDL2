package com.foogle.adnansakel.bdl_food_app.Tabs;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.foogle.adnansakel.bdl_food_app.Adapters.NewsFeedListAdapter;
import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.foogle.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.foogle.adnansakel.bdl_food_app.PostDetailsActivity;
import com.foogle.adnansakel.bdl_food_app.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by Adnan Sakel on 5/4/2016.
 */
public class NewsFeed extends Fragment {

   // Context context;
   ListView lvNewsFeed;
    ProgressDialog progress;
    int newsFeedSortRule = 0;
    NewsFeedListAdapter newsfeedlistadapter;
    View v;
    Spinner spSortRule;
    String[] arrSpSortRule = {"All", "Buy Requests", "Sell Requests", "Post Time", "Price", "Servings", "Location"};
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_newsfeed,container,false);
        if(v.findViewById(R.id.llheader).getVisibility() == View.VISIBLE){
            v.findViewById(R.id.llheader).setVisibility(View.GONE);
        }
        //Toast.makeText(getContext(),"From fragment newsfeed",Toast.LENGTH_LONG).show();
        newsfeedlistadapter = new NewsFeedListAdapter(getContext());
        lvNewsFeed = (ListView)v.findViewById(R.id.lvNewsfeed);
        lvNewsFeed.setAdapter(newsfeedlistadapter);

        lvNewsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeedData newsFeedData = new NewsFeedData();
                newsFeedData = (NewsFeedData) newsfeedlistadapter.getItem(position);
                startActivity(new Intent(getContext(), PostDetailsActivity.class).putExtra(AppConstants.POST_DETAILS, newsFeedData));

            }
        });

        progress = ProgressDialog.show(getContext(), null,
                null, true);
        progress.setContentView(R.layout.progressdialogview);
        progress.setCancelable(true);
        spSortRule = (Spinner)v.findViewById(R.id.spinner_sort_newsfeed);
        ArrayAdapter<String> spSrAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arrSpSortRule);
        spSortRule.setAdapter(spSrAdapter);
        spSortRule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                //if()
                {
                    newsFeedSortRule = position;
                }
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
                newsFeedSortRule = 0;
            }
        });
        loadNewsFeed();
        return v;
    }

    public void loadNewsFeed(){

        Firebase.setAndroidContext(getContext());
        Firebase newsfeedRef = new Firebase("https://bdlfoodapp.firebaseio.com/Posts");

        progress.show();

        Query queryRef = newsfeedRef.orderByKey().limitToLast(50);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //System.out.println("Hello");
                System.out.println(dataSnapshot.toString());
                //for(DataSnapshot post : dataSnapshot.getChildren()){
                NewsFeedData nfd = new NewsFeedData();
                // PostData pd = dataSnapshot.getValue(PostData.class); not working; don't know why
                nfd.setDishName(dataSnapshot.child("DishName").getValue().toString());
                nfd.setLocation(dataSnapshot.child("Location").getValue().toString());
                nfd.setNumberofDishes(dataSnapshot.child("NumberofDishes").getValue().toString());
                nfd.setOrderBefore(dataSnapshot.child("OrderBefore").getValue().toString());
                if(dataSnapshot.child("Price").getValue().toString().length()==0){nfd.setPrice("50");}//for the time being some dummy price
                else nfd.setPrice(dataSnapshot.child("Price").getValue().toString());
                nfd.setPostMessage(dataSnapshot.child("PostMessage").getValue().toString());

                String Image_String = (String) dataSnapshot.child("Image").getValue();
                if(Image_String != null){
                    nfd.setImage(Image_String);}
                nfd.setIngredients(dataSnapshot.child("Ingredients").getValue().toString());
                nfd.setPostKey(dataSnapshot.getKey().toString());
                nfd.setPostOwner_UserID(dataSnapshot.child("UserID").getValue().toString());
                nfd.setPostOwner_FirebaseKey(dataSnapshot.child("FirebaseUserKey").getValue().toString());
                nfd.setPostOwnerName(dataSnapshot.child("PostOwnerName").getValue().toString());
                nfd.setPostOwnerEmail(dataSnapshot.child("Email").getValue().toString());
                //System.out.println("Dish: " + nfd.getDishName());
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


    /*public void setContext(Context context){
        this.context = context;
        v.findViewById(R.id.)
    }*/


}
