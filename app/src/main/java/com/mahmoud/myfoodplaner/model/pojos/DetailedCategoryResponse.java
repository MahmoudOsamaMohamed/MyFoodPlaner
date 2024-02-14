package com.mahmoud.myfoodplaner.model.pojos;

import com.google.j2objc.annotations.Property;

import java.util.List;

public class DetailedCategoryResponse {
    @Property("categories")
    private List<DetailedCategory> categories ;

    public List<DetailedCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DetailedCategory> categories) {
        this.categories = categories;
    }
}
