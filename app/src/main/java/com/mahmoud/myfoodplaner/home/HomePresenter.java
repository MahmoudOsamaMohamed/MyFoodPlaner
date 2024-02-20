package com.mahmoud.myfoodplaner.home;

import android.content.Context;
import android.util.Log;


import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.callbacks.AreasCallback;
import com.mahmoud.myfoodplaner.model.callbacks.CategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.AreaPojo;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements LongMealsCallback , ShortMealsCallback, CategoriesCallback, AreasCallback {
    private HomeView homeView;
    private MealsRemoteDataSource mealsRemoteDataSource;


    public HomePresenter(HomeView homeView, MealsRemoteDataSource mealsRemoteDataSource, Context context) {
        this.homeView = homeView;
        this.mealsRemoteDataSource = mealsRemoteDataSource;


    }

public void getAllAreas(){

    mealsRemoteDataSource.getAllAreas(this);
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
    public void getMealsByArea(String area){

        mealsRemoteDataSource.getAllMealsByArea(area,this);
    }
    public void getAreas(){

        mealsRemoteDataSource.getAllAreas(this);
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

    @Override
    public void onSuccessAreasResult(List<AreaPojo> areaPojos) {
        homeView.setAreas(areaPojos);
        Log.i("sucess", areaPojos.get(0).getStrArea());
    }

    @Override
    public void onFailureAreasResult(String error) {
        Log.i("badness",error);
    }
}
