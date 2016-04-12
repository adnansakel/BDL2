package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;


public class UserProfileActivity extends Activity implements View.OnClickListener{

    private TextView txt_view_user_details;
    Button bSave;
    Button bBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        initializeComponent();
    }

    private void initializeComponent(){
        txt_view_user_details = (TextView) findViewById(R.id.text_view_user_details_header);

        bSave = (Button) findViewById(R.id.button_save_profile_updates);
        bBack = (Button) findViewById(R.id.button_back_from_profile);

        bSave.setOnClickListener(this);
        bBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v == bSave){
            startActivity(new Intent(UserProfileActivity.this, NewPostActivity.class));
            this.finish();
        }

        if (v == bBack)
        {
            startActivity(new Intent(UserProfileActivity.this, NewsFeedActivity.class));
            this.finish();
        }
    }
}
