package com.foogle.adnansakel.bdl_food_app.Tabs;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.foogle.adnansakel.bdl_food_app.Adapters.OrderListAdapter;
import com.foogle.adnansakel.bdl_food_app.DataModel.AppConstants;
import com.foogle.adnansakel.bdl_food_app.DataModel.OrderData;
import com.foogle.adnansakel.bdl_food_app.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Adnan Sakel on 5/4/2016.
 */
public class OrderList extends Fragment {
    ListView lvOrderList;
    OrderListAdapter orderListAdapter;
    OrderData orderData;
    ProgressDialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_orderlist,container,false);

        if(v.findViewById(R.id.llheader).getVisibility() == View.VISIBLE){
            v.findViewById(R.id.llheader).setVisibility(View.GONE);
        }

        orderListAdapter = new OrderListAdapter(getContext());
        lvOrderList = (ListView)v.findViewById(R.id.lvOrderList);
        lvOrderList.setAdapter(orderListAdapter);
        progress = ProgressDialog.show(getContext(), null,
                null, true);
        progress.setContentView(R.layout.progressdialogview);
        progress.setCancelable(true);
        loadOrderListData();
        return v;
    }

    private void loadOrderListData(){
        progress.show();
        //Toast.makeText(getActivity(),"Load order data",Toast.LENGTH_SHORT).show();
        Firebase.setAndroidContext(getContext());
        new Firebase(AppConstants.FirebaseUri+"/"+AppConstants.USERS+"/"+AppConstants.FirebaseUserkey+"/"+AppConstants.ORDER_TO)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        //System.out.println("OrderTo:\n"+dataSnapshot.getValue());
                        DataSnapshot ds = dataSnapshot;//dataSnapshot.child("OrderTo");
                        //orderListAdapter.clearAll();
                        //for (DataSnapshot ds : ordertodata.getChildren()) {
                        //System.out.println(ds.getValue());
                        orderData = new OrderData();
                        orderData.setDishName(ds.child("DishName").getValue().toString());
                        orderData.setBuyorSell("Buy");//as it is OrderTo
                        orderData.setLocation(ds.child("Location").getValue().toString());
                        orderData.setPostID(ds.child("PostID").getValue().toString());
                        orderData.setPrice(ds.child("Price").getValue().toString());
                        orderData.setUserID(ds.child("UserID").getValue().toString());
                        orderData.setValidTill(ds.child("ValidTill").getValue().toString());
                        orderData.setUserName(ds.child("PostOwnerName").getValue().toString());
                        //System.out.println("PostID: " + orderData.getPostID().toString());
                        //System.out.println("UserID: "+orderData.getUserID().toString());

                        orderListAdapter.addItem(orderData);
                        progress.dismiss();


                        // }
                        /*
                        DataSnapshot orderfromdata = dataSnapshot.child("OrderFrom");
                        for (DataSnapshot ds : orderfromdata.getChildren()) {
                            //System.out.println(ds.getValue());
                            orderData = new OrderData();
                            orderData.setDishName(ds.child("DishName").getValue().toString());
                            orderData.setBuyorSell("Sell");//as it is OrderTo
                            orderData.setLocation(ds.child("Location").getValue().toString());
                            orderData.setPostID(ds.child("PostID").getValue().toString());
                            orderData.setPrice(ds.child("Price").getValue().toString());
                            orderData.setUserID(ds.child("UserID").getValue().toString());
                            orderData.setValidTill(ds.child("ValidTill").getValue().toString());
                            orderData.setUserName(ds.child("PostOwnerName").getValue().toString());
                            //System.out.println("PostID: " + orderData.getPostID().toString());
                            //System.out.println("UserID: "+orderData.getUserID().toString());

                            orderListAdapter.addItem(orderData);


                        }*/


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        new Firebase(AppConstants.FirebaseUri + "/" + AppConstants.USERS + "/" + AppConstants.FirebaseUserkey + "/" + AppConstants.ORDER_FROM)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //System.out.println("OrderFrom:\n"+dataSnapshot.getValue());
                        DataSnapshot ds = dataSnapshot;
                        //for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //System.out.println(ds.getValue());
                            orderData = new OrderData();
                            orderData.setDishName(ds.child("DishName").getValue().toString());
                            orderData.setBuyorSell("Sell");//as it is Orderfrom
                            orderData.setLocation(ds.child("Location").getValue().toString());
                            orderData.setPostID(ds.child("PostID").getValue().toString());
                            orderData.setPrice(ds.child("Price").getValue().toString());
                            orderData.setUserID(ds.child("UserID").getValue().toString());
                            orderData.setValidTill(ds.child("ValidTill").getValue().toString());
                            orderData.setUserName("" + ds.child("BuyerName").getValue().toString());
                            //System.out.println("PostID: " + orderData.getPostID().toString());
                            //System.out.println("UserID: " + orderData.getUserID().toString());
                            orderListAdapter.addItem(orderData);
                            progress.dismiss();

                      //  }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }


                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }
}
