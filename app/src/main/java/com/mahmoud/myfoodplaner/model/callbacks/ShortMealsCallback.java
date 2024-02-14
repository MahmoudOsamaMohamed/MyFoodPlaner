package com.mahmoud.myfoodplaner.model.callbacks;

import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import java.util.List;

public interface ShortMealsCallback {
    void onSuccessShortMealsResult(List<ShortMeal> meals);
    void onFailureShortMealsResult(String error);
}
