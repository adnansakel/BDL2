package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Sakel on 3/29/2016.
 */
public class NewPostActivity extends Activity implements View.OnClickListener {

    LinearLayout llNewsFeed;
    LinearLayout llNewPost;
    LinearLayout llOrders;

    View viewNewPost;
    View viewNewsFeed;
    View viewOrders;

    Button buttonPost;

    EditText editTextDishName;
    EditText editTextCategory;
    EditText editTextIngredients;
    EditText editTextNumberofDishes;
    EditText editTextPostMessage;

    RadioButton radioButtonBuy;
    RadioButton radioButtonSell;

    ProgressDialog progress;

    Map<String,Object> post;

    Firebase postRef;

    String BuyorSell = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        initializeComponent();
    }

    private void initializeComponent(){

        llNewsFeed = (LinearLayout)findViewById(R.id.linear_layout_news_feed);
        llNewPost = (LinearLayout)findViewById(R.id.linear_layout_new_post);
        llOrders = (LinearLayout)findViewById(R.id.llOrder);

        viewNewsFeed = (View)findViewById(R.id.view_news_feed);
        viewNewPost = (View)findViewById(R.id.view_new_post);
        viewOrders = (View)findViewById(R.id.viewOrders);

        buttonPost = (Button)findViewById(R.id.button_post);

        editTextDishName = (EditText)findViewById(R.id.editText_DishName);
        editTextCategory = (EditText)findViewById(R.id.editText_Category);
        editTextIngredients = (EditText)findViewById(R.id.editTextIngredients);
        editTextNumberofDishes = (EditText)findViewById(R.id.edittext_NumberofDishes);
        editTextPostMessage = (EditText)findViewById(R.id.editText_postMessage);

        radioButtonBuy = (RadioButton)findViewById(R.id.radioButton_Buy);
        radioButtonSell = (RadioButton)findViewById(R.id.radioButton_Sell);

        llNewPost.setBackgroundColor(Color.parseColor("#33ffffff"));
        viewNewPost.setBackgroundResource(R.drawable.add_new_white);

        llNewsFeed.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewsFeed.setBackgroundResource(R.drawable.home_black);

        llOrders.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewOrders.setBackgroundResource(R.drawable.cart_black);

        viewNewsFeed.setOnClickListener(this);
        viewOrders.setOnClickListener(this);
       // buttonPost.setOnClickListener(this);

        radioButtonBuy.setOnClickListener(this);
        radioButtonSell.setOnClickListener(this);

        post = new HashMap<String,Object>();

        Firebase.setAndroidContext(this);

        postRef = new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.POSTS);

    }

    @Override
    public void onClick(View v) {
        if(v == viewNewsFeed){
            startActivity(new Intent(NewPostActivity.this, NewsFeedActivity.class));
            this.finish();
        }
        if(v == viewOrders){
            startActivity(new Intent(NewPostActivity.this, OrderListActivity.class));
            this.finish();
        }
        if( v == buttonPost){

            final Map<String,Object> post = new HashMap<String,Object>();
            post.put(AppConstants.USER_ID,AppConstants.UserID);
            post.put("FirebaseUserKey",AppConstants.FirebaseUserkey);
            post.put("Location","Kista");//some dummy data for the time being
            post.put("OrderBefore","15.00 22-06-2016");//time should be saved as a number yyyymmddhhmm
            post.put("DishName",editTextDishName.getText().toString());
            post.put("Category",editTextCategory.getText().toString());
            post.put("Ingredients",editTextIngredients.getText().toString());
            post.put("Image","http://somedummyurl/dummyimage");//image data should be saved in firebase
            post.put("NumberofDishes",editTextNumberofDishes.getText().toString());
            post.put("PostMessage",editTextPostMessage.getText().toString());
            post.put("BuyorSell",BuyorSell);
            post.put("Price","50");
            post.put(AppConstants.ORDER_FROM,"");

            progress = ProgressDialog.show(this, null,
                    null, true);
            progress.setContentView(R.layout.progressdialogview);
            progress.setCancelable(true);
            postRef.push().setValue(post,new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        //System.out.println("Data could not be saved. " + firebaseError.getMessage());
                        progress.dismiss();
                        Toast.makeText(NewPostActivity.this, "Some error occured while posting", Toast.LENGTH_LONG);
                    } else {
                        //System.out.println("Data saved successfully.");
                        post.put("PostKey", firebase.getKey().toString());
                        new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.USERS+"/"+AppConstants.FirebaseUserkey+"/"+AppConstants.POSTS)
                                .push().setValue(post, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    //error occured
                                    progress.dismiss();


                                } else {
                                    //success
                                    progress.dismiss();
                                    startActivity(new Intent(NewPostActivity.this, NewsFeedActivity.class));
                                    NewPostActivity.this.finish();
                                }
                            }
                        });


                    }
                }
            });

        }
        else if( v == radioButtonBuy){
            BuyorSell = "Buy";
        }
        else if( v == radioButtonSell){
            BuyorSell = "Sell";
        }
    }
}
