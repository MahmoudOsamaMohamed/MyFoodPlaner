package com.mahmoud.myfoodplaner.home;

import com.mahmoud.myfoodplaner.dbmodels.Area;
import com.mahmoud.myfoodplaner.dbmodels.Category;
import com.mahmoud.myfoodplaner.dbmodels.Ingerdiant;
import com.mahmoud.myfoodplaner.model.pojos.AreaPojo;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import java.util.List;

public interface HomeView {

    void showRandomMeal(List<LongMeal> longMeals);
    void showRandomMealError(String error);
   void showCategories(List<Category> simpleCategories);
   void showCategoriesError(String error);
   void showAreas(List<Area>areas);
   void showAreasError(String error);
   void showIngrediants(List<Ingerdiant>ingerdiants);
   void showIngrediantsError(String error);
    void showShortMealsError(String error);

    void setAreas(List<AreaPojo> areaPojos);
}
