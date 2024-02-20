package com.mahmoud.myfoodplaner.dbmodels.favourate;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourates",indices = @Index(value = {"mealName"},unique = true))
public class Favourate {
    @PrimaryKey
    @NonNull
    public String id;
    @ColumnInfo(name = "mealName")
    public String mealName;

    public String img_url;

}
