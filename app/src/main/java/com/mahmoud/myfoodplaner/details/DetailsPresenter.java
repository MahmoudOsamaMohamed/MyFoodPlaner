package com.mahmoud.myfoodplaner.details;

import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.dbmodels.favourate.FavourateLocalDataSource;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailsPresenter implements LongMealsCallback {
    private DetailsView detailsView;
    private MealsRemoteDataSource detailsRemoteDataSource;
    private FavourateLocalDataSource favourateLocalDataSource;

    public DetailsPresenter(DetailsView detailsView, MealsRemoteDataSource detailsRemoteDataSource, FavourateLocalDataSource favourateLocalDataSource) {
        this.detailsView = detailsView;
        this.favourateLocalDataSource = favourateLocalDataSource;
        this.detailsRemoteDataSource = detailsRemoteDataSource;
    }

    @Override
    public void onSuccessLongMealsResult(List<LongMeal> longMeals) {
        detailsView.showMealDetails(longMeals.get(0));
    }

    @Override
    public void onFailureLongMealsResult(String error) {

    }

    public void getMealDetails(String id){

        detailsRemoteDataSource.getMealsByID(id,this);
    }
    public void addToFavourites(Favourate favourate){
        favourateLocalDataSource.insertFavourate(favourate).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Snackbar.make(detailsView.getView2(), "Added To Favourites", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }
}
