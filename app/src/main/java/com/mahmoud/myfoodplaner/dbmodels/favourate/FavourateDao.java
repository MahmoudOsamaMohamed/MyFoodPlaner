package com.mahmoud.myfoodplaner.dbmodels.favourate;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavourateDao {
    @Query("SELECT * FROM favourates")
    Flowable<List<Favourate>>getAllFavourate();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertFavourate(Favourate favourate);
    @Query("select * from favourates where mealName = :name")
    Flowable<List<Favourate>>getFavourateById(String name);
    @Query("delete from favourates where mealName = :name")
    Completable deleteFavourate(String name);
    @Query("delete from favourates")
    Completable deleteAllFavourate();
}
