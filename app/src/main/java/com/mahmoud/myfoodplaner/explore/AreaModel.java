package com.mahmoud.myfoodplaner.explore;

import android.graphics.Bitmap;

public class AreaModel {
    private String name;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public AreaModel(String name) {
        this.name = name;


    }
}
