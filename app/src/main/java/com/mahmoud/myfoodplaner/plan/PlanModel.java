package com.mahmoud.myfoodplaner.plan;

import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import java.util.List;

public class PlanModel {

    private List<LongMeal> longMealList;
    private String day;

    public void setLongMealList(List<LongMeal> longMealList) {
        this.longMealList = longMealList;
    }



    public PlanModel(List<LongMeal> itemList, String day) {
        this.longMealList = itemList;
        this.day = day;

    }


    public List<LongMeal> getLongMealList() {
        return longMealList;
    }

    public String getDay() {
        return day;
    }


}
