package com.mahmoud.myfoodplaner.dbmodels.table;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class TableLocalDataSource {
    private TableDao tableDao;
    private static TableLocalDataSource INSTANCE;
    private static RoomDB db;
    private TableLocalDataSource(Context context) {
        db = RoomDB.getDatabase(context);
        tableDao = db.getTableDao();
    }
    public static synchronized TableLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TableLocalDataSource(context);
        }
        return INSTANCE;
    }
    public Flowable<List<table>> getAllTables(){
        return tableDao.getAllTables();
    }
    public Completable insertTable(table table){
        return tableDao.insertTable(table);
    }
    public Flowable<List<table>> getTableByDay(String day){
        return tableDao.getTableByDay(day);
    }
    public Completable deleteAllTables(){

        return tableDao.deleteAllTables();
    }
    public Completable deleteTableByMealName(String mealName){

        return tableDao.deleteTableByMealName(mealName);
    }
}
