package com.mahmoud.myfoodplaner.homerefactor;

public class HomeItem {
    private String imageUrl;
    private String name;
    private String description;
    private int type;

    public HomeItem(String imageUrl, String name, String description, int type) {
        this.type = type;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
