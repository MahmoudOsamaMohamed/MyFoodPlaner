package com.mahmoud.myfoodplaner.model.callbacks;

import com.mahmoud.myfoodplaner.model.pojos.DetailedCategory;

import java.util.List;

public interface DetailedCategoriesCallback {
    void onSuccessDetailedCategoriesResult(List<DetailedCategory> categories);
    void onFailureDetailedCategoriesResult(String error);
}
