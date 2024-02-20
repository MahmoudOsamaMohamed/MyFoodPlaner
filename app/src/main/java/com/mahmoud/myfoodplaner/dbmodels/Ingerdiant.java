package com.mahmoud.myfoodplaner.dbmodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingerdiants")
public class Ingerdiant {

    @PrimaryKey
    @NonNull
    public String name;
    public String image;
}
