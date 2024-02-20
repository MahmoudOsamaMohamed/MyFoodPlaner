package com.mahmoud.myfoodplaner.dbmodels.favourate;

import android.content.Context;

import com.mahmoud.myfoodplaner.dbmodels.table.RoomDB;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class FavourateLocalDataSource {
    private static FavourateLocalDataSource INSTANCE;
    private static RoomDB db;
    private FavourateDao favourateDao;
    private FavourateLocalDataSource(Context context) {
        db = RoomDB.getDatabase(context);
        favourateDao = db.favourateDao();
    }
    public static synchronized FavourateLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FavourateLocalDataSource(context);
        }
        return INSTANCE;
    }
    public Flowable<List<Favourate>> getAllFavourate(){
        return favourateDao.getAllFavourate();
    }
    public Completable insertFavourate(Favourate favourate){

        return favourateDao.insertFavourate(favourate);
    }
    public Completable deleteFavourate(String mealName){


        return favourateDao.deleteFavourate(mealName);
    }
    public Flowable<List<Favourate>> getFavourateById(String id){

        return favourateDao.getFavourateById(id);
    }
    public Completable deleteAllFavourate(){

        return favourateDao.deleteAllFavourate();
    }
}
