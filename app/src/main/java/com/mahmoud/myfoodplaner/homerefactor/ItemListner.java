package com.mahmoud.myfoodplaner.homerefactor;

public interface ItemListner {
    int CATEGORY = 0;
    int AREA = 1;
    int INGREDIANT = 2;
    void onItemClicked(String name,int type);
}
