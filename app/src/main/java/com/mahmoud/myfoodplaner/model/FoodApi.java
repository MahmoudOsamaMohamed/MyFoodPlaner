package com.mahmoud.myfoodplaner.model;



import com.mahmoud.myfoodplaner.model.pojos.DetailedCategoryResponse;
import com.mahmoud.myfoodplaner.model.pojos.LongMealResponse;
import com.mahmoud.myfoodplaner.model.pojos.AreaResponse;
import com.mahmoud.myfoodplaner.model.pojos.IngredientResponse;
import com.mahmoud.myfoodplaner.model.pojos.ShortMealResponse;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategoryResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {
    @GET("list.php?")
    Observable<SimpleCategoryResponse> getAllCategories(@Query("c") String param );
    @GET("filter.php?")
    Observable<ShortMealResponse> getAllMealsByCategory(@Query("c") String category);
    @GET("search.php?")
    Observable<LongMealResponse> getMealsByName(@Query("s") String name);
    @GET("lookup.php?")
    Observable<LongMealResponse> getMealsByID(@Query("i") String id);
    @GET("filter.php?")
    Observable<ShortMealResponse> getAllMealsByIngredient(@Query("i") String ingredient);
    @GET("filter.php?")
    Observable<ShortMealResponse> getAllMealsByArea(@Query("a") String area);
    @GET("random.php")
    Observable<LongMealResponse>getRandomMeal();
    @GET("list.php?")
    Observable<AreaResponse> getAllAreas(@Query("a") String param);
    @GET("list.php?")
    Observable<IngredientResponse> getAllIngredients(@Query("i") String ingredient);
    @GET("categories.php")
    Observable<DetailedCategoryResponse> getDetailedCategories();

}
