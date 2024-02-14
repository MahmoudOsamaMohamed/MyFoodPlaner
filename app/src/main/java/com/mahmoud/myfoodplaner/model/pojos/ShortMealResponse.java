package com.mahmoud.myfoodplaner.model.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShortMealResponse {
    @SerializedName("meals")
    private ArrayList<ShortMeal> shortMeals;

    public ArrayList<ShortMeal> getShortMeals() {
        return shortMeals;
    }

    public void setShortMeals(ArrayList<ShortMeal> shortMeals) {
        this.shortMeals = shortMeals;
    }
}
