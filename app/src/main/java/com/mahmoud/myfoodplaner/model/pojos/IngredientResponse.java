package com.mahmoud.myfoodplaner.model.pojos;

import com.google.gson.annotations.SerializedName;
import com.google.j2objc.annotations.Property;

import java.util.List;

public class IngredientResponse {
    @SerializedName("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
