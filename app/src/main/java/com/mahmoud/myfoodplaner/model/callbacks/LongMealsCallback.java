package com.mahmoud.myfoodplaner.model.callbacks;

import com.mahmoud.myfoodplaner.model.pojos.LongMeal;

import java.util.List;

public interface LongMealsCallback {

    void onSuccessLongMealsResult(List<LongMeal> longMeals);
    void onFailureLongMealsResult(String error);
}
