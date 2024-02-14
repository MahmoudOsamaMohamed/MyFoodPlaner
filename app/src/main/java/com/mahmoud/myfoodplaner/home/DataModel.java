package com.mahmoud.myfoodplaner.home;

import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import java.util.List;

public class DataModel {

    private List<ShortMeal> shortMealList;
    private String categortyName;

    public void setShortMealList(List<ShortMeal> shortMealList) {
        this.shortMealList = shortMealList;
    }

    private boolean isExpandable;

    public DataModel(List<ShortMeal> itemList, String categortyName) {
        this.shortMealList = itemList;
        this.categortyName = categortyName;
        isExpandable = false;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<ShortMeal> getShortMealList() {
        return shortMealList;
    }

    public String getCategortyName() {
        return categortyName;
    }

    public boolean isExpandable() {
        return isExpandable;
    }
}
