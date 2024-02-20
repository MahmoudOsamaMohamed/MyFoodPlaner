package com.mahmoud.myfoodplaner.favourate;

import android.content.Context;

import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.dbmodels.favourate.FavourateLocalDataSource;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouratePresenter implements LongMealsCallback {
    private FavourateView view;
    private FavourateLocalDataSource favourateLocalDataSource;
    private MealsRemoteDataSource mealsRemoteDataSource;
    public FavouratePresenter(FavourateView view, Context context) {
        this.view = view;
        favourateLocalDataSource = FavourateLocalDataSource.getInstance(context);
        mealsRemoteDataSource = MealsRemoteDataSourceImpl.getInstance();

    }
    public void getAllFavourate(){
        favourateLocalDataSource.getAllFavourate().
        take(1) .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
               subscribe(new FlowableSubscriber<List<Favourate>>() {
                   @Override
                   public void onSubscribe(@NonNull Subscription s) {
                       s.request(1);
                   }

                   @Override
                   public void onNext(List<Favourate> favourates) {
                       view.showAllFavourate(favourates);
                   }

                   @Override
                   public void onError(Throwable t) {

                   }

                   @Override
                   public void onComplete() {

                   }
               });
    }
    public void deleteFavourate(String mealName){
        favourateLocalDataSource.deleteFavourate(mealName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
        getAllFavourate();
    }
    public void getMealByName(String name){
        mealsRemoteDataSource.getMealsByName(name,this);
    }

    @Override
    public void onSuccessLongMealsResult(List<LongMeal> longMeals) {
        view.navigateWith(longMeals);
    }

    @Override
    public void onFailureLongMealsResult(String error) {

    }
}
