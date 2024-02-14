package com.mahmoud.myfoodplaner.model.callbacks;

import com.mahmoud.myfoodplaner.model.pojos.Ingredient;

import java.util.List;

public interface IngredientsCallback {

    void onSuccessIngredientsResult(List<Ingredient> ingredients);
    void onFailureIngredientsResult(String error);
}
