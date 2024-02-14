package com.mahmoud.myfoodplaner.model.pojos;

import java.util.List;

public class LongMealResponse {
    private List<LongMeal> meals ;
    public List<LongMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<LongMeal> meals) {
        this.meals = meals;
    }


}
