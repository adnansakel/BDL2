package com.foogle.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Created by Adnan Sakel on 3/29/2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private Button button_sign_in;
    private Button button_sign_up;

    private EditText editText_email;
    private EditText editText_password;

    private String email = "";
    private String password = "";

    Context context;

    Firebase loginref;

    ProgressDialog progress;

    SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponent();
    }

    private void initializeComponent() {
        button_sign_in = (Button) findViewById(R.id.button_sign_in);
        button_sign_up = (Button) findViewById(R.id.button_sign_up);

        editText_email = (EditText) findViewById(R.id.edit_text_email);
        editText_password = (EditText) findViewById(R.id.edit_text_password);

        button_sign_in.setOnClickListener(this);
        button_sign_up.setOnClickListener(this);

        userPref = getSharedPreferences(AppConstants.USER_INFO, Context.MODE_PRIVATE);
        this.getSavedUserInfo();

        Firebase.setAndroidContext(this);
        loginref = new Firebase(AppConstants.FirebaseUri);

        context = getApplicationContext();

        /*Firebase ref = new Firebase("https://android-chat.firebaseio-demo.com/chat");
        Query queryRef = ref.orderByChild("author").equalTo("JavaUser6564");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                System.out.println("Query result:" + snapshot);
                int i = 1;
                for( DataSnapshot sn : snapshot.getChildren()){
                    System.out.println("Query result "+(i++)+ " :" + sn.getKey());
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });*/


    }


    private void saveUserInfo() {
        SharedPreferences.Editor editUsrInfo = userPref.edit();
        editUsrInfo.putString(AppConstants.USER_EMAIL, email);
        editUsrInfo.putString(AppConstants.USER_PASSWORD, password);
        editUsrInfo.apply();
    }
    private void getSavedUserInfo() {
        String user_email = userPref.getString(AppConstants.USER_EMAIL, AppConstants.DEFAULT_NULL);
        String user_password = userPref.getString(AppConstants.USER_PASSWORD, AppConstants.DEFAULT_NULL);
        if( (!user_email.equals(AppConstants.DEFAULT_NULL)) && (!user_password.equals(AppConstants.DEFAULT_NULL)) )
        {
            editText_email.setText(user_email);
            editText_password.setText(user_password);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.saveUserInfo();
    }

    @Override
    public void onClick(View v) {
        if(v == button_sign_in){
            email = editText_email.getText().toString();
            AppConstants.Email = email;
            password = editText_password.getText().toString();
            progress = ProgressDialog.show(this, null,
                    null, true);
            progress.setContentView(R.layout.progressdialogview);
            progress.setCancelable(true);
            loginref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                    AppConstants.UserID = authData.getUid();


                    Query ifUserAlreadyExist = new Firebase(AppConstants.FirebaseUri + "/" + AppConstants.USERS).orderByChild("UserID").equalTo(AppConstants.UserID);
                    ifUserAlreadyExist.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //System.out.println(dataSnapshot.toString());
                            if (dataSnapshot.getValue() == null) {
                                progress.dismiss();
                                Toast.makeText(LoginActivity.this, "User does not exist. Please sign up", Toast.LENGTH_LONG).show();

                            }
                            else{
                                //AppConstants.FirebaseUserkey = ((DataSnapshot)dataSnapshot.getValue()).getKey().toString();
                                for(DataSnapshot ds:dataSnapshot.getChildren()){

                                    AppConstants.FirebaseUserkey = ds.getKey().toString();
                                    AppConstants.UserName = ds.child("UserName").getValue().toString();

                                }

                                progress.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                //startActivity(new Intent(LoginActivity.this, OrderListActivity.class));
                                LoginActivity.this.finish();

                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            progress.dismiss();
                        }
                    });


                    }

                    @Override
                    public void onAuthenticationError (FirebaseError firebaseError){
                        // there was an error
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this, "Could not authenticate user. May be you need to sign up", Toast.LENGTH_LONG).show();
                    }
                }

                );
            }
            if(v == button_sign_up){
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

        }
    }
}
