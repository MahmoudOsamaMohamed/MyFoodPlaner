package com.mahmoud.myfoodplaner.home;

import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import java.util.List;

public interface HomeView {

    void showRandomMeal(List<LongMeal> longMeals);
    void showRandomMealError(String error);
    void setCategorieslist(List<SimpleCategory>categorieslist);
    void showCategoriesError(String error);
    void updateCategoryBrowse(List<ShortMeal>shortMealsList);
    void showShortMealsError(String error);
    
}
