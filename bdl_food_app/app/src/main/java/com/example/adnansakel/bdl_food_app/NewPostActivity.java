package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.example.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Sakel on 3/29/2016.
 */
public class NewPostActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
NumberPicker.OnValueChangeListener{

    LinearLayout llSettings;
    View viewSettings;
    LinearLayout llNewsFeed;
    View viewNewsFeed;
    LinearLayout llNewPost;
    View viewNewPost;
    LinearLayout llOrders;
    View viewOrders;

    Button buttonPost;

    EditText editTextDishName;
    EditText editTextCategory;
    EditText editTextIngredients;
    //EditText editTextNumberofDishes;
    EditText editTextPostMessage;

    RadioButton radioButtonBuy;
    RadioButton radioButtonSell;

    ProgressDialog progress;

    Map<String,Object> post;

    Firebase postRef;

    String BuyorSell = "";
    String postLocation = "";

    Integer nbrOfServings = 0;
    EditText etServings;

    Integer OrderBeforeTimeHours, OrderBeforeTimeMinutes;
    EditText etTimeHours, etTimeMinutes;

    Integer OrderBeforeDateDay, OrderBeforeDateMonth, OrderBeforeDateYear;
    EditText etDateDay, etDateMonth;


    Spinner spLocation;
    String[] arraySpinner = {"Kista", "Sollentuna", "Barkarby", "Solna", "Morby", "Akalla", "Hellenlund", "Rinkeby", "Tensta"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        initializeComponent();
    }

    private void initializeComponent(){

        llNewsFeed = (LinearLayout)findViewById(R.id.linear_layout_news_feed);
        llNewPost = (LinearLayout)findViewById(R.id.linear_layout_new_post);
        llSettings = (LinearLayout)findViewById(R.id.linear_layout_settings);
        llOrders = (LinearLayout)findViewById(R.id.llOrder);

        viewNewsFeed = (View)findViewById(R.id.view_news_feed);
        viewNewPost = (View)findViewById(R.id.view_new_post);
        viewOrders = (View)findViewById(R.id.viewOrders);
        viewSettings = (View)findViewById(R.id.view_settings);

        llNewPost.setBackgroundColor(Color.parseColor("#33ffffff"));
        viewNewPost.setBackgroundResource(R.drawable.add_new_white);

        llNewsFeed.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewsFeed.setBackgroundResource(R.drawable.home_black);

        llOrders.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewOrders.setBackgroundResource(R.drawable.cart_black);

        llSettings.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewSettings.setBackgroundResource(R.drawable.settingsblack);

        llNewsFeed.setOnClickListener(this);
        viewSettings.setOnClickListener(this);

        buttonPost = (Button)findViewById(R.id.button_post);

        editTextDishName = (EditText)findViewById(R.id.editText_DishName);
        editTextCategory = (EditText)findViewById(R.id.editText_Category);
        editTextIngredients = (EditText)findViewById(R.id.editTextIngredients);
        //editTextNumberofDishes = (EditText)findViewById(R.id.textView_NumberofDishes);
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
        buttonPost.setOnClickListener(this);

        radioButtonBuy.setOnClickListener(this);
        radioButtonSell.setOnClickListener(this);

        etServings = (EditText)findViewById(R.id.editText_Servings);
        etTimeHours = (EditText)findViewById(R.id.editText_time_hours);
        etTimeMinutes = (EditText)findViewById(R.id.editText_time_minutes);
        etDateDay = (EditText)findViewById(R.id.editText_date_day);
        etDateMonth = (EditText)findViewById(R.id.editText_date_month);

        spLocation = (Spinner)findViewById(R.id.spinner_location);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, arraySpinner);
        spLocation.setAdapter(spAdapter);
        spLocation.setOnItemSelectedListener(this);

        post = new HashMap<String,Object>();

        Firebase.setAndroidContext(this);

        postRef = new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.POSTS);

    }

    private String readOrderBeforeInfo(){
        String timedate = "";

        try {
            OrderBeforeTimeHours = Integer.parseInt(etTimeHours.getText().toString());
        }
        catch(NumberFormatException e){ OrderBeforeTimeHours = 0;}

        try {
            OrderBeforeTimeMinutes = Integer.parseInt(etTimeMinutes.getText().toString());
        }
        catch(NumberFormatException e){ OrderBeforeTimeMinutes = 0;}

        try {
            OrderBeforeDateDay = Integer.parseInt(etDateDay.getText().toString());
        }
        catch(NumberFormatException e){ OrderBeforeDateDay = 0;}

        try {
            OrderBeforeDateMonth = Integer.parseInt(etDateMonth.getText().toString());
        }
        catch(NumberFormatException e){ OrderBeforeDateMonth = 0;}

        OrderBeforeDateYear = 2016;

        if((OrderBeforeTimeHours < 0) || (OrderBeforeTimeHours > 24) ||
                (OrderBeforeTimeMinutes > 60) || (OrderBeforeTimeMinutes < 0)
                || ((OrderBeforeTimeHours == 24) && (OrderBeforeTimeMinutes != 0)))

        {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Invalid Time!", Toast.LENGTH_LONG);
            toast.show();
        }
        else if((OrderBeforeDateDay <= 0) || (OrderBeforeDateDay > 31) ||
                (OrderBeforeDateMonth <= 0) || (OrderBeforeDateMonth > 12)){
            Toast toast = Toast.makeText(this.getApplicationContext(), "Invalid Date!", Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            timedate = "Time: " + OrderBeforeTimeHours.toString() + ":" + OrderBeforeTimeMinutes.toString() +
                    " " + "Date: " +  OrderBeforeDateDay.toString() + "-" + OrderBeforeDateMonth.toString() +
                    "-" +  OrderBeforeDateYear.toString();
        }

        return timedate;
    }

    @Override
    public void onClick(View v) {
        if(v == viewNewsFeed){
            startActivity(new Intent(NewPostActivity.this, NewsFeedActivity.class));
            this.finish();
        }
        else if(v == viewSettings){
            startActivity(new Intent(NewPostActivity.this, SettingsMenuActivity.class));
            this.finish();
        }
        if(v == viewOrders){
            startActivity(new Intent(NewPostActivity.this, OrderListActivity.class));
            this.finish();
        }
        if( v == buttonPost){

            String dishName, dishCategory, timeDateString;
            dishName = editTextDishName.getText().toString();
            dishCategory = editTextCategory.getText().toString();
            try {
                nbrOfServings = Integer.parseInt(etServings.getText().toString());
            }
            catch(NumberFormatException e){ nbrOfServings = 0;}

            timeDateString = readOrderBeforeInfo();

            if(dishName.equals(""))
            {
                Toast toast = Toast.makeText(this.getApplicationContext(), "Please Enter a Dish Name!", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(dishCategory.equals(""))
            {
                Toast toast = Toast.makeText(this.getApplicationContext(), "Please Provide Dish Category!", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(nbrOfServings == 0)
            {
                Toast toast = Toast.makeText(this.getApplicationContext(), "Please Enter Number of Servings!", Toast.LENGTH_LONG);
                toast.show();
            }
            else if (timeDateString.equals ("")){
                //toast will appear from readOrderBeforeInfo()
            }
            else
            {
                final Map<String,Object> post = new HashMap<String,Object>();
                post.put(AppConstants.USER_ID,AppConstants.UserID);
                post.put("FirebaseUserKey",AppConstants.FirebaseUserkey);
                post.put("Location", postLocation);//some dummy data for the time being
                post.put("OrderBefore", timeDateString);//time should be saved as a number yyyymmddhhmm
                post.put("DishName", dishName);
                post.put("Category", dishCategory);
                post.put("Ingredients",editTextIngredients.getText().toString());
                post.put("Image","http://somedummyurl/dummyimage");//image data should be saved in firebase
                post.put("NumberofDishes",nbrOfServings);
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
        }
        else if( v == radioButtonBuy){
            BuyorSell = "Buy";
        }
        else if( v == radioButtonSell){
            BuyorSell = "Sell";
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // do your other stuff depends on the new value
        nbrOfServings = newVal;
    }
    @Override
    public void onItemSelected(AdapterView parent, View v, int position, long id) {
        //if()
        {
            postLocation = arraySpinner[position];
        }
    }
    @Override
    public void onNothingSelected(AdapterView parent) {
            postLocation = "Kista";
    }
}
