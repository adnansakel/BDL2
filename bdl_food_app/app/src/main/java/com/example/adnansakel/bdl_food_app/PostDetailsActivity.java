package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.example.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Sakel on 4/7/2016.
 */

public class PostDetailsActivity extends Activity implements View.OnClickListener{
    private Button bBuyButton;
    private ImageView imgFoodPicture;
    private TextView txtFoodPrice;
    private TextView txtLocation;
    private TextView txtFoodIngredients;
    private TextView txtPostDetails;
    private TextView txtDishName;
    private NewsFeedData newsFeedData;
    private String PostWonerFirebaseKey;
    ProgressDialog progress;
    Map<String,Object> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        newsFeedData = new NewsFeedData();
        newsFeedData = (NewsFeedData)this.getIntent().getSerializableExtra(AppConstants.POST_DETAILS);
        initializeComponent();
    }

    private void initializeComponent(){
        bBuyButton = (Button)findViewById(R.id.buyButton);

        imgFoodPicture = (ImageView)findViewById(R.id.imageViewFoodImage);

        txtFoodPrice = (TextView)findViewById(R.id.textViewItemPrice);

        txtLocation = (TextView)findViewById(R.id.textViewLocation);

        txtFoodIngredients = (TextView)findViewById(R.id.textViewFoodIngredients);

        txtPostDetails = (TextView)findViewById(R.id.textViewFoodPostDetails);

        txtDishName = (TextView)findViewById(R.id.textViewDishName);

        bBuyButton.setOnClickListener(this);

        txtFoodPrice.setText(newsFeedData.getPrice());
        txtDishName.setText(newsFeedData.getDishName());
        txtFoodIngredients.setText(newsFeedData.getIngredients());
        txtFoodPrice.setText(newsFeedData.getPrice()+" SEK");
        txtLocation.setText(newsFeedData.getLocation());
        txtPostDetails.setText(newsFeedData.getPostMessage());

        order = new HashMap<String,Object>();
        order.put(AppConstants.USER_ID, "");
        order.put(AppConstants.POST_ID, newsFeedData.getPostKey());//post ID of the post being purchased
        order.put("Location",newsFeedData.getLocation());
        order.put("ValidTill",newsFeedData.getOrderBefore());
        order.put("Price",newsFeedData.getPrice());
        order.put("DishName",newsFeedData.getDishName());

        Firebase.setAndroidContext(this);
    }


    @Override
    public void onClick(View v) {
        if(v == bBuyButton)
        {
            startActivity(new Intent(PostDetailsActivity.this,PayPal_selection.class).putExtra(AppConstants.POST_DETAILS,newsFeedData));
            this.finish();
            //progress = ProgressDialog.show(this, null,
             //       null, true);
            //progress.setContentView(R.layout.progressdialogview);
            //progress.setCancelable(true);
            /*order.put(AppConstants.USER_ID, newsFeedData.getPostOwner_UserID());//user id of the post owner or seller
            new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.USERS+"/"+AppConstants.FirebaseUserkey+"/"+AppConstants.ORDER_TO).push().setValue(order,
                    new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if(firebaseError != null){
                                progress.dismiss();
                            }
                            else{
                                //success

                                order.put(AppConstants.USER_ID,AppConstants.UserID);//person who is buying it
                                new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.POSTS+"/"+newsFeedData.getPostKey()+"/"+AppConstants.ORDER_FROM)
                                        .push()
                                        .setValue(order, new Firebase.CompletionListener() {
                                            @Override
                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                if(firebaseError !=null){
                                                    progress.dismiss();
                                                }
                                                else{
                                                    //success
                                                    new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.USERS+"/"+newsFeedData.getPostOwner_FirebaseKey()
                                                    +"/"+AppConstants.ORDER_FROM).push().setValue(order, new Firebase.CompletionListener() {
                                                        @Override
                                                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                            if(firebaseError != null){
                                                                progress.dismiss();
                                                            }
                                                            else{
                                                                //success
                                                                progress.dismiss();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        }
                    });*/
        }
    }

    @Override
    public void onBackPressed(){
            startActivity(new Intent(PostDetailsActivity.this,NewsFeedActivity.class));
            this.finish();
    }

}