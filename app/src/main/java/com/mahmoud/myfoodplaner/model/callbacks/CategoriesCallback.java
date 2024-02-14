package com.mahmoud.myfoodplaner.model.callbacks;

import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import java.util.List;

public interface CategoriesCallback {
    void onSuccessCategoriesResult(List<SimpleCategory> categories);
    void onFailureCategoriesResult(String error);
}
