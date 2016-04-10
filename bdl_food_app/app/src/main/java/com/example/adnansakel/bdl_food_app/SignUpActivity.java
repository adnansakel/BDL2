package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

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
            email = editText_email.getText().toString();
            password = editText_password.getText().toString();
            verifyPassword = editText_verify_password.getText().toString();
            if(password.equals(verifyPassword)){
                signup_ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        //System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        //should add initial user profile in firebase here
                        SignUpActivity.this.finish();
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

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
