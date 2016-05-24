package com.foogle.adnansakel.bdl_food_app.Tabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.foogle.adnansakel.bdl_food_app.Image;
import com.foogle.adnansakel.bdl_food_app.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Sakel on 5/4/2016.
 */
public class NewPost extends Fragment {

    private static final int IMAGE_TAKEN = 9;
    LinearLayout llSettings;
    View viewSettings;
    LinearLayout llNewsFeed;
    View viewNewsFeed;
    LinearLayout llNewPost;
    View viewNewPost;
    LinearLayout llOrders;
    View viewCamera;
    View viewOrders;

    Button buttonPost;
    View viewPrice;

    EditText editTextDishName;
    EditText editTextCategory;
    EditText editTextIngredients;
    //EditText editTextNumberofDishes;
    EditText editTextPostMessage;
    EditText editTextLocation;

    RadioButton radioButtonBuy;
    RadioButton radioButtonSell;

    private  String encodeImage;
    ProgressDialog progress;

    View view_set_price;

    Map<String,Object> post;

    Firebase postRef;

    String BuyorSell = "";
    String postLocation = "";
    String price = "";

    Integer nbrOfServings = 0;
    EditText etServings;

    Integer OrderBeforeTimeHours, OrderBeforeTimeMinutes;
    EditText etTimeHours, etTimeMinutes;

    Integer OrderBeforeDateDay, OrderBeforeDateMonth, OrderBeforeDateYear;
    EditText etDateDay, etDateMonth;
    View v;
    LayoutInflater inflater;
    Spinner spLocation;
    String[] arraySpinner = {"Kista", "Sollentuna", "Barkarby", "Solna", "Morby", "Akalla", "Hellenlund", "Rinkeby", "Tensta"};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_newpost,container,false);

        if(v.findViewById(R.id.llheader).getVisibility() == View.VISIBLE){
            v.findViewById(R.id.llheader).setVisibility(View.GONE);
        }
        initializeComponent();
        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView ivImage;
        ivImage = (ImageView) (v.findViewById(R.id.ivImage));
        if (requestCode == IMAGE_TAKEN) {
            if(resultCode == getActivity().RESULT_OK){
                encodeImage  =data.getStringExtra("encoded_image");
                byte[] imageAsBytes = Base64.decode(encodeImage, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                ivImage.setImageBitmap(bmp);

            }
            else{
                //Toast.makeText(getActivity(),"Test....",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeComponent(){

        ImageView ivImage;
        ivImage = (ImageView) (v.findViewById(R.id.ivImage));

        Bitmap bitmap = ((BitmapDrawable)ivImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        byte[] image=stream.toByteArray();
        encodeImage = Base64.encodeToString(image, 0);

        llNewsFeed = (LinearLayout)v.findViewById(R.id.linear_layout_news_feed);
        llNewPost = (LinearLayout)v.findViewById(R.id.linear_layout_new_post);
        llSettings = (LinearLayout)v.findViewById(R.id.linear_layout_settings);
        llOrders = (LinearLayout)v.findViewById(R.id.llOrder);

        viewNewsFeed = (View)v.findViewById(R.id.view_news_feed);
        viewNewPost = (View)v.findViewById(R.id.view_new_post);
        viewOrders = (View)v.findViewById(R.id.viewOrders);
        viewCamera = (View)v.findViewById(R.id.camera_view);
        viewSettings = (View)v.findViewById(R.id.view_settings);

        llNewPost.setBackgroundColor(Color.parseColor("#33ffffff"));
        viewNewPost.setBackgroundResource(R.drawable.add_new_white);

        llNewsFeed.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewsFeed.setBackgroundResource(R.drawable.home_black);

        llOrders.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewOrders.setBackgroundResource(R.drawable.cart_black);

        llSettings.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewSettings.setBackgroundResource(R.drawable.settingsblack);

        //llNewsFeed.setOnClickListener(this);
        //viewSettings.setOnClickListener(this);

        buttonPost = (Button)v.findViewById(R.id.button_post);
        viewPrice = v.findViewById(R.id.viewPrice);



        editTextDishName = (EditText)v.findViewById(R.id.editText_DishName);
        editTextCategory = (EditText)v.findViewById(R.id.editText_Category);
        editTextIngredients = (EditText)v.findViewById(R.id.editTextIngredients);
        //editTextNumberofDishes = (EditText)findViewById(R.id.textView_NumberofDishes);
        editTextPostMessage = (EditText)v.findViewById(R.id.editText_postMessage);

        radioButtonBuy = (RadioButton)v.findViewById(R.id.radioButton_Buy);
        radioButtonSell = (RadioButton)v.findViewById(R.id.radioButton_Sell);

        llNewPost.setBackgroundColor(Color.parseColor("#33ffffff"));
        viewNewPost.setBackgroundResource(R.drawable.add_new_white);

        llNewsFeed.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewNewsFeed.setBackgroundResource(R.drawable.home_black);

        llOrders.setBackgroundColor(Color.parseColor("#00ffffff"));
        viewOrders.setBackgroundResource(R.drawable.cart_black);
        editTextLocation = (EditText)v.findViewById(R.id.editTextLocation);



        viewPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                inflater = getActivity().getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                view_set_price = inflater.inflate(R.layout.setting_price_layout, null);
                builder.setView(view_set_price)
                        // Add action buttons
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                price = ((EditText)
                                        (view_set_price).findViewById(R.id.editText_price)).getText().toString();
                            Toast.makeText(getActivity(),"Price set to "+price + " SEK",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.create();

                builder.show();

            }
        });

        //viewNewsFeed.setOnClickListener(this);
        //viewOrders.setOnClickListener(this);

        viewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), Image.class), IMAGE_TAKEN);
            }
        });
        radioButtonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyorSell = "Buy";
            }
        });
        radioButtonSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyorSell = "Sell";
            }
        });

        etServings = (EditText)v.findViewById(R.id.editText_Servings);
        etTimeHours = (EditText)v.findViewById(R.id.editText_time_hours);
        etTimeMinutes = (EditText)v.findViewById(R.id.editText_time_minutes);
        etDateDay = (EditText)v.findViewById(R.id.editText_date_day);
        etDateMonth = (EditText)v.findViewById(R.id.editText_date_month);

        spLocation = (Spinner)v.findViewById(R.id.spinner_location);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        spLocation.setAdapter(spAdapter);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });

        post = new HashMap<String,Object>();

        Firebase.setAndroidContext(getContext());

        postRef = new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.POSTS);

        buttonPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(AppConstants.IsExploring){
                    Toast.makeText(getContext(),"Please login first to be able to post",Toast.LENGTH_LONG).show();
                    return;
                }
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
                    Toast toast = Toast.makeText(getContext(), "Please Enter a Dish Name!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(dishCategory.equals(""))
                {
                    Toast toast = Toast.makeText(getContext(), "Please Provide Dish Category!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(nbrOfServings == 0)
                {
                    Toast toast = Toast.makeText(getContext(), "Please Enter Number of Servings!", Toast.LENGTH_LONG);
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
                    if(editTextLocation.getText().length()>0){postLocation = editTextLocation.getText().toString();}
                    else{
                        postLocation = "Kista";
                    }
                    post.put("Location", postLocation);//some dummy data for the time being
                    post.put("OrderBefore", timeDateString);//time should be saved as a number yyyymmddhhmm
                    post.put("DishName", dishName);
                    post.put("Category", dishCategory);
                    post.put("Ingredients",editTextIngredients.getText().toString());
                    post.put("Image",encodeImage);
                    post.put("NumberofDishes",nbrOfServings);
                    post.put("PostMessage",editTextPostMessage.getText().toString());
                    post.put("BuyorSell",BuyorSell);
                    if(price.length()==0){price = "50";}
                    post.put("Price",price);
                    post.put("PostOwnerName",AppConstants.UserName);
                    post.put("Email",AppConstants.Email);
                    post.put(AppConstants.ORDER_FROM,"");

                    progress = ProgressDialog.show(getContext(), null,
                            null, true);
                    progress.setContentView(R.layout.progressdialogview);
                    progress.setCancelable(true);
                    postRef.push().setValue(post, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                //System.out.println("Data could not be saved. " + firebaseError.getMessage());
                                progress.dismiss();
                                Toast.makeText(getContext(), "Some error occured while posting", Toast.LENGTH_LONG);
                            } else {
                                //System.out.println("Data saved successfully.");
                                post.put("PostKey", firebase.getKey().toString());
                                new Firebase(AppConstants.FirebaseUri + "/" + AppConstants.USERS + "/" + AppConstants.FirebaseUserkey + "/" + AppConstants.POSTS)
                                        .push().setValue(post, new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError != null) {
                                            //error occured
                                            progress.dismiss();

                                        } else {
                                            //success
                                            progress.dismiss();
                                            //startActivity(new Intent(NewPostActivity.this, NewsFeedActivity.class));
                                            //NewPostActivity.this.finish();
                                            Toast.makeText(getContext(), "Food offer posted successfully.", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });


                            }
                        }
                    });
                }
            }
        });

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
            Toast toast = Toast.makeText(getContext(), "Invalid Time!", Toast.LENGTH_LONG);
            toast.show();
        }
        else if((OrderBeforeDateDay <= 0) || (OrderBeforeDateDay > 31) ||
                (OrderBeforeDateMonth <= 0) || (OrderBeforeDateMonth > 12)){
            Toast toast = Toast.makeText(getContext(), "Invalid Date!", Toast.LENGTH_LONG);
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


}
