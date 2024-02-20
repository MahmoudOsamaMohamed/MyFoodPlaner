package com.mahmoud.myfoodplaner.plan;

import android.content.Context;
import android.util.Log;

import com.mahmoud.myfoodplaner.dbmodels.table.TableLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.table;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.*;

public class PlanPresenter implements LongMealsCallback, ShortMealsCallback {
    TableLocalDataSource tableLocalDataSource;
    MealsRemoteDataSource mealsRemoteDataSource;
    PlanView planView;
    public PlanPresenter(Context context, MealsRemoteDataSource mealsRemoteDataSource, PlanView planView) {
        this.tableLocalDataSource = TableLocalDataSource.getInstance(context);
        this.mealsRemoteDataSource = mealsRemoteDataSource;
        this.planView = planView;
    }
   public void getDayMeals(String day){
       tableLocalDataSource.getTableByDay(day).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(new FlowableSubscriber<List<table>>() {
                   @Override
                   public void onSubscribe(@NonNull Subscription s) {
                       s.request(1);
                        Log.i("getDayMeals","subs");
                   }

                   @Override
                   public void onNext(List<table> tables) {
                        Log.i("getDayMeals","onNext "+ tables.size()+" "+day);
                        planView.showDayMeals(tables);
                   }

                   @Override
                   public void onError(Throwable t) {
                       Log.i("getDayMeals",t.getMessage());
                   }

                   @Override
                   public void onComplete() {
                       Log.i("getDayMeals","onComplete");
                   }
               });
   }

    public void getMealByName(String name){
        mealsRemoteDataSource.getMealsByName(name,this);
    }
    @Override
    public void onSuccessLongMealsResult(List<LongMeal> longMeals) {
        planView.showMeal(longMeals);
    }

    @Override
    public void onFailureLongMealsResult(String error) {
        Log.i("getLongMealsfromPLan",error);
    }

    @Override
    public void onSuccessShortMealsResult(List<ShortMeal> meals) {

    }

    @Override
    public void onFailureShortMealsResult(String error) {

    }
}
