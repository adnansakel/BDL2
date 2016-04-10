package com.example.adnansakel.bdl_food_app.DataModel;

/**
 * Created by Adnan Sakel on 3/31/2016.
 */
public class PostData {

    private String UseID;
    private String Location;
    private String OrderBefore;
    private String DishName;
    private String Category;
    private String Ingredients;
    private String ImageUrl;
    private String NumberofDishes;
    private String PostMessage;
    private String BuyorSell;


    public PostData(){

    }

    public String getUseID() {
        return UseID;
    }

    public String getLocation() {
        return Location;
    }

    public String getOrderBefore() {
        return OrderBefore;
    }

    public String getDishName() {
        return DishName;
    }

    public String getCategory() {
        return Category;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getNumberofDishes() {
        return NumberofDishes;
    }

    public String getPostMessage() {
        return PostMessage;
    }

    public String getBuyorSell() {
        return BuyorSell;
    }
}
