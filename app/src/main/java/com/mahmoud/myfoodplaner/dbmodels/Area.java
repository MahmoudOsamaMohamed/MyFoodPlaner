package com.mahmoud.myfoodplaner.dbmodels;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "areas")
public class Area {

    @PrimaryKey
    @NonNull

    public String name;
}
