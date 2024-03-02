package com.mahmoud.myfoodplaner.home;

import android.content.Context;
import android.util.Log;


import com.mahmoud.myfoodplaner.dbmodels.Area;
import com.mahmoud.myfoodplaner.dbmodels.CashingLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.Category;
import com.mahmoud.myfoodplaner.dbmodels.Ingerdiant;
import com.mahmoud.myfoodplaner.explore.CategoryAdpater;
import com.mahmoud.myfoodplaner.explore.CategoryModel;
import com.mahmoud.myfoodplaner.explore.ExploreFragment;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.callbacks.AreasCallback;
import com.mahmoud.myfoodplaner.model.callbacks.CategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.AreaPojo;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements LongMealsCallback , ShortMealsCallback {
    private HomeView homeView;
    private MealsRemoteDataSource mealsRemoteDataSource;
    private Context context;


    public HomePresenter(HomeView homeView, MealsRemoteDataSource mealsRemoteDataSource, Context context) {
        this.homeView = homeView;
        this.mealsRemoteDataSource = mealsRemoteDataSource;
        this.context = context;


    }
public void getAllAreas(){
    CashingLocalDataSource.getInstance(context).getAllAreas().take(1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new FlowableSubscriber<List<Area>>() {
                @Override
                public void onSubscribe(@NonNull Subscription s) {
                    s.request(1);
                }

                @Override
                public void onNext(List<Area> areas) {

                    homeView.showAreas(areas);
                }

                @Override
                public void onError(Throwable t) {
                    homeView.showAreasError(t.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
}
    public void getAllIngredients(){
        CashingLocalDataSource.getInstance(context).getAllIngerdiants().take(1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<List<Ingerdiant>>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<Ingerdiant> ingerdiants) {

                        homeView.showIngrediants(ingerdiants);
                    }

                    @Override
                    public void onError(Throwable t) {
                        homeView.showIngrediantsError(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void getAllCategories(){
        CashingLocalDataSource.getInstance(context).getAllCategories().take(1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<Category>>() {

                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<Category> categories) {

                        homeView.showCategories(categories);
                    }

                    @Override
                    public void onError(Throwable t) {
                        homeView.showCategoriesError(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void getRandomMeal(){

        mealsRemoteDataSource.getRandomMeal(this);
    }

    public void getMealsByCategory(String category){

        mealsRemoteDataSource.getAllMealsByCategory(category,this);
    }
    public void getMealsByArea(String area){

        mealsRemoteDataSource.getAllMealsByArea(area,this);
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
    public void onSuccessShortMealsResult(List<ShortMeal> meals) {

    }

    @Override
    public void onFailureShortMealsResult(String error) {
        homeView.showShortMealsError(error);
    }


}
