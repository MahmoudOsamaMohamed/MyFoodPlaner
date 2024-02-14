package com.mahmoud.myfoodplaner.model;

import com.mahmoud.myfoodplaner.model.pojos.LongMealResponse;
import com.mahmoud.myfoodplaner.model.pojos.AreaResponse;
import com.mahmoud.myfoodplaner.model.pojos.IngredientResponse;
import com.mahmoud.myfoodplaner.model.pojos.ShortMealResponse;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {
    @GET("list.php?")
    Call<SimpleCategoryResponse> getAllCategories(@Query("c") String param );
    @GET("filter.php?")
    Call<ShortMealResponse> getAllMealsByCategory(@Query("c") String category);
    @GET("search.php?")
    Call<LongMealResponse> getMealsByName(@Query("s") String name);
    @GET("lookup.php?")
    Call<LongMealResponse> getMealsByID(@Query("i") String id);
    @GET("filter.php?")
    Call<ShortMealResponse> getAllMealsByIngredient(@Query("i") String ingredient);
    @GET("filter.php?")
    Call<ShortMealResponse> getAllMealsByArea(@Query("a") String area);
    @GET("random.php")
    Call<LongMealResponse>getRandomMeal();
    @GET("list.php?")
    Call<AreaResponse> getAllAreas(@Query("a") String area);
    @GET("list.php?")
    Call<IngredientResponse> getAllIngredients(@Query("i") String ingredient);

}
