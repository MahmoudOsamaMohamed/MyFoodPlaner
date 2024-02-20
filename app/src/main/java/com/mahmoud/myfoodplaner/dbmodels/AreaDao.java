package com.mahmoud.myfoodplaner.dbmodels;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface AreaDao {
    @Query("SELECT * FROM areas")
    Flowable<List<Area>> getAllAreas();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertArea(Area area);
}
