package com.example.adnansakel.bdl_food_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.example.adnansakel.bdl_food_app.DataModel.NewsFeedData;


public class PayPal_selection extends ActionBarActivity implements View.OnClickListener {

    NewsFeedData newsFeedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_selection);

        newsFeedData = new NewsFeedData();
        newsFeedData = (NewsFeedData)this.getIntent().getSerializableExtra(AppConstants.POST_DETAILS);

        Button button_paypal_sdk= (Button) findViewById(R.id.button_paypal_sdk);
        button_paypal_sdk.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_paypal_sdk:
                // call paypal sdk intent
                startActivity(new Intent(PayPal_selection.this,PayPal_sdk.class).putExtra(AppConstants.POST_DETAILS,newsFeedData));
                this.finish();
                break;


            default:

                break;



        }

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(PayPal_selection.this,NewsFeedActivity.class));
        this.finish();
    }
}
