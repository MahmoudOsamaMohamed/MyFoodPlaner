package com.mahmoud.myfoodplaner.dbmodels;

import androidx.room.Entity;

import androidx.annotation.NonNull;

import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey
    @NonNull
    public String name;
    public String img_url;
    public String description;
}