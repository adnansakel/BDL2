package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.example.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class PayPal_sdk extends ActionBarActivity {

    Button button_paywithpaypal;
    EditText editText_friend_name;
    EditText editText_amount;

    NewsFeedData newsFeedData;
    ProgressDialog progress;
    Map<String,Object> order;
    private static final int REQUEST_CODE_PAYMENT = 1;

    // PayPal configuration
   /* private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(
                    Util.paypal_sdk_id);*/
    private static PayPalConfiguration paypalConfig=new PayPalConfiguration()
    .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
    .clientId( Util.paypal_sdk_id)
    .acceptCreditCards(true)
    // The following are only used in PayPalFuturePaymentActivity.
    .merchantName("Code_Crash")
    .merchantPrivacyPolicyUri(
            Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(
            Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_sdk);

        newsFeedData = new NewsFeedData();
        newsFeedData = (NewsFeedData)this.getIntent().getSerializableExtra(AppConstants.POST_DETAILS);
        Firebase.setAndroidContext(this);
        // Starting PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);

        editText_friend_name= (EditText) findViewById(R.id.editText_friend_name);
        editText_amount= (EditText) findViewById(R.id.editText_amount);

        button_paywithpaypal= (Button) findViewById(R.id.button_paywithpaypal);

        button_paywithpaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                order = new HashMap<String,Object>();
                order.put(AppConstants.USER_ID, "");
                order.put(AppConstants.POST_ID, newsFeedData.getPostKey());//post ID of the post being purchased
                order.put("Location",newsFeedData.getLocation());
                order.put("ValidTill",newsFeedData.getOrderBefore());
                order.put("Price",newsFeedData.getPrice());
                order.put("DishName",newsFeedData.getDishName());
                order.put(AppConstants.USER_ID, newsFeedData.getPostOwner_UserID());
                progress = ProgressDialog.show(PayPal_sdk.this, null,
                        null, true);
                progress.setContentView(R.layout.progressdialogview);
                progress.setCancelable(true);
                new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.USERS+"/"+AppConstants.FirebaseUserkey+"/"+AppConstants.ORDER_TO).push().setValue(order,
                        new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    progress.dismiss();
                                } else {
                                    //success

                                    order.put(AppConstants.USER_ID, AppConstants.UserID);//person who is buying it
                                    new Firebase(AppConstants.FirebaseUri + "/" + AppConstants.POSTS + "/" + newsFeedData.getPostKey() + "/" + AppConstants.ORDER_FROM)
                                            .push()
                                            .setValue(order, new Firebase.CompletionListener() {
                                                @Override
                                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                    if (firebaseError != null) {
                                                        progress.dismiss();
                                                    } else {
                                                        //success
                                                        new Firebase(AppConstants.FirebaseUri + "/" + AppConstants.USERS + "/" + newsFeedData.getPostOwner_FirebaseKey()
                                                                + "/" + AppConstants.ORDER_FROM).push().setValue(order, new Firebase.CompletionListener() {
                                                            @Override
                                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                if (firebaseError != null) {
                                                                    progress.dismiss();
                                                                } else {
                                                                    //success
                                                                    progress.dismiss();
                                                                    Toast.makeText(getApplicationContext(), "Payment done succesfully ", Toast.LENGTH_LONG).show();
                                                                    startActivity(new Intent(PayPal_sdk.this, OrderListActivity.class));
                                                                    PayPal_sdk.this.finish();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                //call pay pal sdk method
                //launchPayPalPayment();
            }
        });




    }


    private void launchPayPalPayment() {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(editText_amount.getText().toString()),"USD", editText_friend_name.getText().toString(),
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(PayPal_sdk.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {



            }

            else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();


            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(PayPal_sdk.this,NewsFeedActivity.class));
        this.finish();
    }

}
