package com.mahmoud.myfoodplaner.dbmodels;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.*;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
@Dao
public interface IngerdiantDao {

    @Query("SELECT * FROM ingerdiants")
    Flowable<List<Ingerdiant>> getAllIngerdiants();
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    Completable insertIngerdiant(Ingerdiant ingerdiant);
@Query("UPDATE ingerdiants SET image = :image WHERE name = :name")
Completable updateIngerdiant(String name, String image);
}
