package com.mahmoud.myfoodplaner.details;

import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;

import java.util.List;

public class DetailsPresenter implements LongMealsCallback {
    private DetailsView detailsView;
    private MealsRemoteDataSource detailsRemoteDataSource;

    public DetailsPresenter(DetailsView detailsView, MealsRemoteDataSource detailsRemoteDataSource) {
        this.detailsView = detailsView;
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
}
