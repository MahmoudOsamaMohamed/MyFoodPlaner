package com.mahmoud.myfoodplaner.plan;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.dbmodels.table.TableLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.table;
import com.mahmoud.myfoodplaner.favourate.DeleteFavourateListener;
import com.mahmoud.myfoodplaner.home.MealClickListener;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanFragment extends Fragment implements PlanView, MealClickListener,AddClickListener , DeleteFavourateListener {

RecyclerView rc;
PlanAdapter adapter;
PlanPresenter planPresenter;
List<PlanModel> model;
int index=0;
String [] days;

    public PlanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planPresenter = new PlanPresenter(getContext(), MealsRemoteDataSourceImpl.getInstance(),this);
        model = new ArrayList<>();
        days = getResources().getStringArray(R.array.days_of_week);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rc=view.findViewById(R.id.rc);
        for(String day:days){
            Log.i("check on array",day);
            planPresenter.getDayMeals(day);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void showDayMeals(List<table> shortMealsList) {

        List<LongMeal> listo=null;
        Log.i("showDayMeals",index+" "+shortMealsList.size() );
        listo = new ArrayList<>();
        for(table table:shortMealsList){


            LongMeal longMeal = new LongMeal();
            longMeal.setStrMeal(table.mealName);
            longMeal.setStrMealThumb(table.img_url);
            longMeal.setIdMeal(table.id);

            listo.add(longMeal);
          //  Log.i("showDayMeals", model.size()+" " );
        }



        if(shortMealsList.isEmpty()){
            model.add(new PlanModel(new ArrayList<>(),days[index++]));
        }else
            model.add(new PlanModel(listo,days[index++]));
        adapter = new PlanAdapter(model, this, this,this);
        rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rc.setAdapter(adapter);
        if(index==7){
            model=new ArrayList<>();
            index=0;}
    }

    @Override
    public void showMeal(List<LongMeal> longMeals) {
        String id = longMeals.get(0).getIdMeal();
        NavHostFragment.findNavController(this).navigate(PlanFragmentDirections.actionPlanFragmentToMealDetailsFragment(id));
    }

    @Override
    public void onMealClick(String mealName) {
        planPresenter.getMealByName(mealName);
    }

    @Override
    public void onAddClick(int position) {
        SharedPreferences prefs = getActivity().getSharedPreferences("weak_day", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("day", getResources().getStringArray(R.array.days_of_week)[position]).apply();
        NavHostFragment.findNavController(this).navigate(R.id.action_planFragment_to_exploreFragment);
    }

    @Override
    public void onDeleteFavourate(String mealName) {
        TableLocalDataSource.getInstance(getContext()).deleteTableByMealName(mealName).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
NavHostFragment.findNavController(PlanFragment.this).navigate(PlanFragmentDirections.actionPlanFragmentSelf());
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }
                        }
                );
    }
}