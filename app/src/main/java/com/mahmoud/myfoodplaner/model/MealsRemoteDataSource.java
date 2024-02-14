package com.mahmoud.myfoodplaner.model;

import com.mahmoud.myfoodplaner.model.callbacks.*;


public interface MealsRemoteDataSource {
    void getAllMealsByCategory(String category, ShortMealsCallback callback);
    void getAllCategories(CategoriesCallback callback);
    void getMealsByName(String name, LongMealsCallback callback);
    void getMealsByID(String id, LongMealsCallback callback);
    void getAllMealsByIngredient(String ingredient, ShortMealsCallback callback);
    void getAllMealsByArea(String area, ShortMealsCallback callback);
    void getRandomMeal(LongMealsCallback callback);
    void getAllAreas(AreasCallback callback);
    void getAllIngredients(IngredientsCallback callback);
}
