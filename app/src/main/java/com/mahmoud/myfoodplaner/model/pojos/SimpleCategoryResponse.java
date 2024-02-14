package com.mahmoud.myfoodplaner.model.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SimpleCategoryResponse {
    @SerializedName("meals")
    private ArrayList<SimpleCategory>simpleCategoryList;

    public ArrayList<SimpleCategory> getSimpleCategoryList() {
        return simpleCategoryList;
    }

    public void setSimpleCategoryList(ArrayList<SimpleCategory> simpleCategoryList) {
        this.simpleCategoryList = simpleCategoryList;
    }
}
