package com.foogle.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Sakel on 3/29/2016.
 */
public class SignUpActivity extends Activity implements View.OnClickListener{

    private Button button_sign_up;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_verify_password;
    Firebase signup_ref;
    private String email = "";
    private String password = "";
    private String verifyPassword = "";
    private String userName = "";
    ProgressDialog progress;

    Context context;
    CharSequence text = "User account created successfully";
    int duration = Toast.LENGTH_SHORT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeComponent();

    }

    private void initializeComponent(){

        button_sign_up = (Button)findViewById(R.id.button_sign_up);
        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_password = (EditText)findViewById(R.id.editText_password);
        editText_verify_password = (EditText)findViewById(R.id.editText_verify_password);

        button_sign_up.setOnClickListener(this);

        context = this.getApplicationContext();

        Firebase.setAndroidContext(this);
        signup_ref = new Firebase(AppConstants.FirebaseUri);
    }


    @Override
    public void onClick(View v) {
        if(v == button_sign_up){
            progress = ProgressDialog.show(this, null,
                    null, true);
            progress.setContentView(R.layout.progressdialogview);
            progress.setCancelable(true);
            email = editText_email.getText().toString();
            password = editText_password.getText().toString();
            verifyPassword = editText_verify_password.getText().toString();
            userName = ((EditText)findViewById(R.id.editText_UserName)).getText().toString();
            AppConstants.UserName = userName;
            if(password.equals(verifyPassword)){
                signup_ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        //System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        //should add initial user profile in firebase here
                        AppConstants.UserID = ""+result.get("uid");
                        Query ifUserAlreadyExist = new Firebase(AppConstants.FirebaseUri + "/" + AppConstants.USERS).orderByChild("UserID").equalTo(AppConstants.UserID);
                        ifUserAlreadyExist.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //System.out.println(dataSnapshot.toString());
                                if (dataSnapshot.getValue() == null) {
                                    Map<String, Object> user = new HashMap<String, Object>();
                                    user.put("UserID", AppConstants.UserID);
                                    user.put("UserName",AppConstants.UserName);
                                    user.put("Posts", "");
                                    user.put("Email",email);
                                    user.put(AppConstants.ORDER_TO, "");//creating some empty tag will be used later on
                                    user.put(AppConstants.ORDER_FROM, "");
                                    new Firebase(AppConstants.FirebaseUri + "/" + AppConstants.USERS).push().setValue(user, new Firebase.CompletionListener() {

                                        @Override
                                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                            if (firebaseError != null) {
                                                progress.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Some error occured while signing in", Toast.LENGTH_LONG);
                                            } else {
                                                AppConstants.FirebaseUserkey = firebase.getKey().toString();
                                                System.out.println(AppConstants.FirebaseUserkey);
                                                progress.dismiss();
                                                SignUpActivity.this.finish();
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();

                                            }
                                        }
                                    });

                                } else {
                                    //AppConstants.FirebaseUserkey = ((DataSnapshot)dataSnapshot.getValue()).getKey().toString();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        AppConstants.FirebaseUserkey = ds.getKey().toString();
                                        Toast.makeText(SignUpActivity.this, "User already exist", Toast.LENGTH_LONG);

                                    }



                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                progress.dismiss();
                            }
                        });

                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        Toast toast = Toast.makeText(context, firebaseError.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }


        }
    }
}
