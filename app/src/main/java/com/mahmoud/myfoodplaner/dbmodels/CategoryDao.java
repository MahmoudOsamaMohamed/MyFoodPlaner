package com.mahmoud.myfoodplaner.dbmodels;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.*;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    Flowable<List<Category>> getAllCategories();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertCategory(Category category);
}
