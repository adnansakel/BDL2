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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ValueEventListener;
import com.foogle.adnansakel.bdl_food_app.Adapters.CustomStringArrayAdapter;
import com.foogle.adnansakel.bdl_food_app.Adapters.NewsFeedListAdapter;
import com.foogle.adnansakel.bdl_food_app.Adapters.NewsFeedSearchListAdapter;
import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.foogle.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.foogle.adnansakel.bdl_food_app.PostDetailsActivity;
import com.foogle.adnansakel.bdl_food_app.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Sakel on 5/4/2016.
 */
public class NewsFeed extends Fragment {

   // Context context;
   ListView lvNewsFeed;
    ListView lvNewsFeedSearch;
    ProgressDialog progress;
    int newsFeedSortRule = 0;
    NewsFeedListAdapter newsfeedlistadapter;
    NewsFeedSearchListAdapter newsFeedSearchListAdapter;
    View v;

    Button btnSearchNewsFeed;
    EditText editTextSearch;
    Spinner spSortRule;
    String[] arrSpSortRule = {"All", "Dish name", "Price", "Location"};
    List<String> custom_spinner_items;
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

        newsFeedSearchListAdapter = new NewsFeedSearchListAdapter(getContext());
        lvNewsFeedSearch = (ListView)v.findViewById(R.id.lvNewsfeedSearch);
        lvNewsFeedSearch.setAdapter(newsFeedSearchListAdapter);

        btnSearchNewsFeed = (Button)v.findViewById(R.id.btnSearchNewsFeed);
        editTextSearch = (EditText)v.findViewById(R.id.edtxtSearch);

        btnSearchNewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = "" + editTextSearch.getText();
                String filter = arrSpSortRule[newsFeedSortRule];
                searchNewsFeedData(filter, value);
            }
        });

        lvNewsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeedData newsFeedData = new NewsFeedData();
                newsFeedData = (NewsFeedData) newsfeedlistadapter.getItem(position);
                startActivity(new Intent(getContext(), PostDetailsActivity.class).putExtra(AppConstants.POST_DETAILS, newsFeedData));

            }
        });

        lvNewsFeedSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeedData newsFeedData = new NewsFeedData();
                newsFeedData = (NewsFeedData) newsFeedSearchListAdapter.getItem(position);
                startActivity(new Intent(getContext(), PostDetailsActivity.class).putExtra(AppConstants.POST_DETAILS, newsFeedData));

            }
        });

        progress = ProgressDialog.show(getContext(), null, null, true);
        progress.setContentView(R.layout.progressdialogview);
        progress.setCancelable(true);
        //progress.dismiss();
        spSortRule = (Spinner)v.findViewById(R.id.spinner_sort_newsfeed);
        custom_spinner_items = new ArrayList<>();
        custom_spinner_items.add("All");custom_spinner_items.add("Dish name");
        custom_spinner_items.add("Price");custom_spinner_items.add("Location");
        CustomStringArrayAdapter spSrAdapter = new CustomStringArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item,android.R.id.text1,custom_spinner_items,getActivity());
        spSortRule.setAdapter(spSrAdapter);
        //spSrAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spSortRule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {

                    newsFeedSortRule = position;
                    if(position == 0){
                        lvNewsFeedSearch.setVisibility(View.INVISIBLE);
                        lvNewsFeed.setVisibility(View.VISIBLE);
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

        //progress.show();

        Query queryRef = newsfeedRef.orderByKey().limitToLast(100);

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
                if (dataSnapshot.child("Price").getValue().toString().length() == 0) {
                    nfd.setPrice("50");
                }//for the time being some dummy price
                else nfd.setPrice(dataSnapshot.child("Price").getValue().toString());
                nfd.setPostMessage(dataSnapshot.child("PostMessage").getValue().toString());

                String Image_String = (String) dataSnapshot.child("Image").getValue();
                if (Image_String != null) {
                    nfd.setImage(Image_String);
                }
                nfd.setIngredients(dataSnapshot.child("Ingredients").getValue().toString());
                nfd.setPostKey(dataSnapshot.getKey().toString());
                nfd.setPostOwner_UserID(dataSnapshot.child("UserID").getValue().toString());
                nfd.setPostOwner_FirebaseKey(dataSnapshot.child("FirebaseUserKey").getValue().toString());
                nfd.setPostOwnerName(dataSnapshot.child("PostOwnerName").getValue().toString());
                nfd.setPostOwnerEmail(dataSnapshot.child("Email").getValue().toString());
                //System.out.println("Dish: " + nfd.getDishName());
                newsfeedlistadapter.addItemAtBeginning(nfd);
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

    private void searchNewsFeedData(String filter, String value){

        Firebase newsfeedRef = new Firebase("https://bdlfoodapp.firebaseio.com/Posts");
        Query queryRef = newsfeedRef.orderByChild("DishName").equalTo(value);

        if(filter.equals(arrSpSortRule[0])){
            lvNewsFeedSearch.setVisibility(View.INVISIBLE);
            return;
        }
        else if(filter.equals(arrSpSortRule[1])){
            queryRef = newsfeedRef.orderByChild("DishName").equalTo(value);
        }
        else if(filter.equals(arrSpSortRule[2])){
            queryRef = newsfeedRef.orderByChild("Price").equalTo(value);
        }
        else if(filter.equals(arrSpSortRule[3])){
            queryRef = newsfeedRef.orderByChild("Location").equalTo(value);
        }

        progress.show();
        newsFeedSearchListAdapter.clearAll();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lvNewsFeed.setVisibility(View.INVISIBLE);
                lvNewsFeedSearch.setVisibility(View.VISIBLE);
                System.out.println(dataSnapshot.getValue());
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    NewsFeedData nfd = new NewsFeedData();
                    // PostData pd = dataSnapshot.getValue(PostData.class); not working; don't know why
                    nfd.setDishName(ds.child("DishName").getValue().toString());
                    nfd.setLocation(ds.child("Location").getValue().toString());
                    nfd.setNumberofDishes(ds.child("NumberofDishes").getValue().toString());
                    nfd.setOrderBefore(ds.child("OrderBefore").getValue().toString());
                    if(ds.child("Price").getValue().toString().length()==0){nfd.setPrice("50");}//for the time being some dummy price
                    else nfd.setPrice(ds.child("Price").getValue().toString());
                    nfd.setPostMessage(ds.child("PostMessage").getValue().toString());

                    String Image_String = (String) ds.child("Image").getValue();
                    if(Image_String != null){
                        nfd.setImage(Image_String);}
                    nfd.setIngredients(ds.child("Ingredients").getValue().toString());
                    nfd.setPostKey(ds.getKey().toString());
                    nfd.setPostOwner_UserID(ds.child("UserID").getValue().toString());
                    nfd.setPostOwner_FirebaseKey(ds.child("FirebaseUserKey").getValue().toString());
                    nfd.setPostOwnerName(ds.child("PostOwnerName").getValue().toString());
                    nfd.setPostOwnerEmail(ds.child("Email").getValue().toString());
                    //System.out.println("Dish: " + nfd.getDishName());
                    newsFeedSearchListAdapter.addItemAtBeginning(nfd);

                }
                progress.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progress.show();
                Toast.makeText(getContext(),"Something went wrong. Could not load searched data",Toast.LENGTH_LONG).show();
            }
        });


    }


    /*public void setContext(Context context){
        this.context = context;
        v.findViewById(R.id.)
    }*/


}
