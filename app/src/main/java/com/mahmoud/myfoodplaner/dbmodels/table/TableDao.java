package com.mahmoud.myfoodplaner.dbmodels.table;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TableDao {
    @Query("SELECT * FROM tables")
    Flowable<List<table>> getAllTables();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertTable(table table);
    @Query("Select * from tables where day = :day")
    Flowable<List<table>> getTableByDay(String day);
    @Query("delete from tables")
    Completable deleteAllTables();
    @Query("delete from tables where mealName = :mealName")
    Completable deleteTableByMealName(String mealName);
}
