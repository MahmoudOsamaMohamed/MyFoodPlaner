package com.mahmoud.myfoodplaner.dbmodels.table;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tables")
public class table {
    @PrimaryKey
    @NonNull
    public String id;
    public String mealName;
    public String Day;
    public String img_url;

}
