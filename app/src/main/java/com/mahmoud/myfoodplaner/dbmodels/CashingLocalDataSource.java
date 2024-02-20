package com.mahmoud.myfoodplaner.dbmodels;

import android.content.Context;
import java.util.List;

import com.mahmoud.myfoodplaner.dbmodels.table.RoomDB;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class CashingLocalDataSource {
    private static CashingLocalDataSource INSTANCE;
    private RoomDB roomDB;
    private CategoryDao categoryDao;
    private AreaDao areaDao;
    private IngerdiantDao ingerdiantDao;
    private CashingLocalDataSource(Context context) {
        roomDB = RoomDB.getDatabase(context);
        categoryDao = roomDB.getCategoryDao();
        areaDao = roomDB.getAreaDao();
        ingerdiantDao = roomDB.getIngredientDao();
    }
    public static synchronized CashingLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CashingLocalDataSource(context);
        }
        return INSTANCE;
    }
    public Flowable<List<Category>> getAllCategories(){
        return categoryDao.getAllCategories();
            }
    public Completable insertCategory(Category category){
        return categoryDao.insertCategory(category);
    }
    public Flowable<List<Area>> getAllAreas(){
        return areaDao.getAllAreas();
    }
    public Completable insertArea(Area area){
        return areaDao.insertArea(area);
    }
    public Flowable<List<Ingerdiant>> getAllIngerdiants(){
        return ingerdiantDao.getAllIngerdiants();
    }
    public Completable insertIngerdiant(Ingerdiant ingerdiant){
        return ingerdiantDao.insertIngerdiant(ingerdiant);
    }
    public Completable updateIngerdiant(String name, String image){
        return ingerdiantDao.updateIngerdiant(name, image);
    }
}
