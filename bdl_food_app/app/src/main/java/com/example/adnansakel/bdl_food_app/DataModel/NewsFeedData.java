package com.example.adnansakel.bdl_food_app.DataModel;

import java.io.Serializable;

/**
 * Created by Adnan Sakel on 3/30/2016.
 */
public class NewsFeedData implements Serializable{

    private String DishName;
    private String Location;
    private String OrderBefore;
    private String Price;
    private String NumberofDishes;
    private String PostMessage;
    private String Ingredients;
    private String PostKey;
    private String PostOwner_UserID;
    private String PostOwner_FirebaseKey;

    public NewsFeedData(){

    }


    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) { Location = location; }

    public String getOrderBefore() {
        return OrderBefore;
    }

    public void setOrderBefore(String orderBefore) {
        OrderBefore = orderBefore;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNumberofDishes() {
        return NumberofDishes;
    }

    public void setNumberofDishes(String numberofDishes) {
        NumberofDishes = numberofDishes;
    }

    public void setPostMessage(String postMessage){ PostMessage = postMessage;}

    public String getPostMessage(){return  PostMessage;}

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getPostKey() {
        return PostKey;
    }

    public void setPostKey(String postKey) {
        PostKey = postKey;
    }

    public String getPostOwner_UserID() {
        return PostOwner_UserID;
    }

    public void setPostOwner_UserID(String userID) {
        PostOwner_UserID = userID;
    }

    public String getPostOwner_FirebaseKey() {
        return PostOwner_FirebaseKey;
    }

    public void setPostOwner_FirebaseKey(String postOwner_FirebaseKey) {
        PostOwner_FirebaseKey = postOwner_FirebaseKey;
    }
}
