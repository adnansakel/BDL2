package com.example.adnansakel.bdl_food_app.DataModel;

/**
 * Created by Adnan Sakel on 4/8/2016.
 */
public class OrderData {

    private String UserID;
    private String Location;
    private String PostID;
    private String ValidTill;
    private String Price;
    private String DishName;
    private String BuyorSell;

    public OrderData(){

    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getValidTill() {
        return ValidTill;
    }

    public void setValidTill(String validTill) {
        ValidTill = validTill;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getBuyorSell() {
        return BuyorSell;
    }

    public void setBuyorSell(String buyorSell) {
        BuyorSell = buyorSell;
    }
}

