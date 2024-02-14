package com.mahmoud.myfoodplaner.home;

import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.callbacks.CategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import java.util.List;

public class HomePresenter implements LongMealsCallback , ShortMealsCallback, CategoriesCallback {
    private HomeView homeView;
    private MealsRemoteDataSource mealsRemoteDataSource;

    public HomePresenter(HomeView homeView, MealsRemoteDataSource mealsRemoteDataSource) {
        this.homeView = homeView;
        this.mealsRemoteDataSource = mealsRemoteDataSource;

    }
    public void getRandomMeal(){

        mealsRemoteDataSource.getRandomMeal(this);
    }
    public void getAllCategories(){
        mealsRemoteDataSource.getAllCategories(this);

    }
    public void getMealsByCategory(String category){
        mealsRemoteDataSource.getAllMealsByCategory(category,this);
    }

    @Override
    public void onSuccessLongMealsResult(List<LongMeal> longMeals) {

        homeView.showRandomMeal(longMeals);
    }

    @Override
    public void onFailureLongMealsResult(String error) {

        homeView.showRandomMealError(error);
    }

    @Override
    public void onSuccessCategoriesResult(List<SimpleCategory> categories) {
        homeView.setCategorieslist(categories);

    }

    @Override
    public void onFailureCategoriesResult(String error) {
        homeView.showCategoriesError(error);
    }

    @Override
    public void onSuccessShortMealsResult(List<ShortMeal> meals) {
        homeView.updateCategoryBrowse(meals);
    }

    @Override
    public void onFailureShortMealsResult(String error) {
        homeView.showShortMealsError(error);
    }
}
