package com.mahmoud.myfoodplaner.explore;

public class MealModel {
    private String name;
    private String image;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MealModel(String name, String image, String id) {
        this.name = name;
        this.image = image;
        this.id = id;
    }
}
