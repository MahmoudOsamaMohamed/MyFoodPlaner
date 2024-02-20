package com.mahmoud.myfoodplaner.dbmodels.table;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.mahmoud.myfoodplaner.dbmodels.*;
import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.dbmodels.favourate.FavourateDao;


@Database(entities = {table.class, Favourate.class, Ingerdiant.class, Area.class, Category.class}, version = 30, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    public abstract TableDao getTableDao();
    public abstract FavourateDao favourateDao();
    public abstract IngerdiantDao getIngredientDao();
    public abstract AreaDao getAreaDao();
    public abstract CategoryDao getCategoryDao();
    private static RoomDB INSTANCE;

    public static RoomDB getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, "myfoodplaner")
                 .fallbackToDestructiveMigration()

                    .build();
        }
        return INSTANCE;
    }
}
