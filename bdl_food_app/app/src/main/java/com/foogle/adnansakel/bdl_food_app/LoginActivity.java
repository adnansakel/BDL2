package com.foogle.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.foogle.adnansakel.bdl_food_app.Helper.ConnectionCheck;

/**
 * Created by Adnan Sakel on 3/29/2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private Button button_sign_in;
    private Button button_sign_up;

    private EditText editText_email;
    private EditText editText_password;

    private LinearLayout llExplore;

    private String email = "";
    private String password = "";

    ConnectionCheck connection;

    Context context;

    Firebase loginref;

    ProgressDialog progress;

    SharedPreferences userPref;

    Handler handler;
    boolean mStopHandler;

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

        llExplore = (LinearLayout)findViewById(R.id.llExplore);
        llExplore.setOnClickListener(this);

        userPref = getSharedPreferences(AppConstants.USER_INFO, Context.MODE_PRIVATE);
        this.getSavedUserInfo();

        connection = new ConnectionCheck(this);
        handler = new Handler();
        mStopHandler = false;

        if(!connection.isConnectedWithoutToastMessage()){
            Toast.makeText(this,"You do not seem to be connected to internet. This app will need internet connection.",Toast.LENGTH_LONG).show();
        }

        Firebase.setAndroidContext(this);
        loginref = new Firebase(AppConstants.FirebaseUri);

        context = getApplicationContext();

        lognInWithSavedCredentials();

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
        //this.saveUserInfo();
    }

    @Override
    public void onClick(View v) {

        if(v == button_sign_in){
            if(!connection.isConnected())return;
            if(editText_email.getText().length() == 0 || editText_password.getText().length() == 0){
                Toast.makeText(this,"Invalid username or password. Please try again.",Toast.LENGTH_LONG).show();
                return;

                }

                tryLogIn(editText_email.getText().toString(),editText_password.getText().toString());

            }
            if(v == button_sign_up){
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

        }
        if( v == llExplore){
            if(!connection.isConnected())return;
            AppConstants.IsExploring = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void tryLogIn( String email_id, String pass){

        email = email_id;
        AppConstants.Email = email;
        password = pass;
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
                                    AppConstants.IsExploring = false;
                                    saveUserInfo();
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
                        Toast.makeText(LoginActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }

        );
    }

    private void lognInWithSavedCredentials(){
        //If no saved credentials return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                if(connection.isConnectedWithoutToastMessage()){
                    String user_email = userPref.getString(AppConstants.USER_EMAIL, AppConstants.DEFAULT_NULL);
                    String user_password = userPref.getString(AppConstants.USER_PASSWORD, AppConstants.DEFAULT_NULL);
                    if( (!user_email.equals(AppConstants.DEFAULT_NULL)) && (!user_password.equals(AppConstants.DEFAULT_NULL)) )
                    {
                        tryLogIn(user_email,user_password);
                    }

                    handler.removeCallbacks(this);
                    mStopHandler = true;

                }
                if (!mStopHandler) {
                    //System.out.println("Hi...");
                    handler.postDelayed(this, 250);
                }
            }
        };

// start it with:
        handler.post(runnable);

    }
}
