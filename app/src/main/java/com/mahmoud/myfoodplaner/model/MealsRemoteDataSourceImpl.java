package com.mahmoud.myfoodplaner.model;

import android.util.Log;

import com.mahmoud.myfoodplaner.model.callbacks.AreasCallback;
import com.mahmoud.myfoodplaner.model.callbacks.CategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.IngredientsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.*;
;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource {
    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    public static final String TAG = "MealsRemoteDataSource";
    Retrofit retrofit;
    private static MealsRemoteDataSource INSTANCE = null;
    private FoodApi foodApi;
    private List<SimpleCategory> categories;
    private List<ShortMeal> shortMeals;
    private List<LongMeal> longMeals;
    private List<Ingredient> ingredients;
    private List<Area> areas;

    private MealsRemoteDataSourceImpl() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).build();
    }

    public static MealsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MealsRemoteDataSourceImpl();

        }
        return INSTANCE;
    }

    @Override
    public void getAllMealsByCategory(String category, ShortMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllMealsByCategory(category).enqueue(new Callback<ShortMealResponse>() {
                @Override
                public void onResponse(Call<ShortMealResponse> call, Response<ShortMealResponse> response) {
                    if (response.isSuccessful()) {
                        shortMeals = response.body().getShortMeals();

                        callback.onSuccessShortMealsResult(shortMeals);
                    }
                }

                @Override
                public void onFailure(Call<ShortMealResponse> call, Throwable t) {

                    callback.onFailureShortMealsResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getAllCategories(CategoriesCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllCategories("list").enqueue(new Callback<SimpleCategoryResponse>() {
                @Override
                public void onResponse(Call<SimpleCategoryResponse> call, Response<SimpleCategoryResponse> response) {
                    if (response.isSuccessful()) {
                        SimpleCategoryResponse simpleCategoryResponse = response.body();
                        categories = simpleCategoryResponse.getSimpleCategoryList();

                        callback.onSuccessCategoriesResult(categories);
                    }
                }

                @Override
                public void onFailure(Call<SimpleCategoryResponse> call, Throwable t) {


                    callback.onFailureCategoriesResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getMealsByName(String name, LongMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getMealsByName(name).enqueue(new Callback<LongMealResponse>() {
                @Override
                public void onResponse(Call<LongMealResponse> call, Response<LongMealResponse> response) {
                    if (response.isSuccessful()) {
                        longMeals = response.body().getMeals();
                        callback.onSuccessLongMealsResult(longMeals);
                    }
                }

                @Override
                public void onFailure(Call<LongMealResponse> call, Throwable t) {

                    callback.onFailureLongMealsResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getMealsByID(String id, LongMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getMealsByID(id).enqueue(new Callback<LongMealResponse>() {
                @Override
                public void onResponse(Call<LongMealResponse> call, Response<LongMealResponse> response) {
                    if (response.isSuccessful()) {
                        longMeals = response.body().getMeals();
                        callback.onSuccessLongMealsResult(longMeals);
                    }
                }

                @Override
                public void onFailure(Call<LongMealResponse> call, Throwable t) {

                    callback.onFailureLongMealsResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getAllMealsByIngredient(String ingredient, ShortMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllMealsByIngredient(ingredient).enqueue(new Callback<ShortMealResponse>() {
                @Override
                public void onResponse(Call<ShortMealResponse> call, Response<ShortMealResponse> response) {
                    if (response.isSuccessful()) {
                        shortMeals = response.body().getShortMeals();
                        callback.onSuccessShortMealsResult(shortMeals);
                    }
                }

                @Override
                public void onFailure(Call<ShortMealResponse> call, Throwable t) {

                    callback.onFailureShortMealsResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getAllMealsByArea(String area, ShortMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllMealsByArea(area).enqueue(new Callback<ShortMealResponse>() {
                @Override
                public void onResponse(Call<ShortMealResponse> call, Response<ShortMealResponse> response) {
                    if (response.isSuccessful()) {
                        shortMeals = response.body().getShortMeals();
                        callback.onSuccessShortMealsResult(shortMeals);
                    }
                }

                @Override
                public void onFailure(Call<ShortMealResponse> call, Throwable t) {

                    callback.onFailureShortMealsResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getRandomMeal(LongMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getRandomMeal().enqueue(new Callback<LongMealResponse>() {
                @Override
                public void onResponse(Call<LongMealResponse> call, Response<LongMealResponse> response) {
                    if (response.isSuccessful()) {
                        longMeals = response.body().getMeals();
                        callback.onSuccessLongMealsResult(longMeals);
                    }
                }

                @Override
                public void onFailure(Call<LongMealResponse> call, Throwable t) {
                    Log.i("meals", t.toString());
                    callback.onFailureLongMealsResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getAllAreas(AreasCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllAreas("list").enqueue(new Callback<AreaResponse>() {
                @Override
                public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                    if (response.isSuccessful()) {
                        areas = response.body().getAreas();
                        callback.onSuccessAreasResult(areas);
                    }
                }

                @Override
                public void onFailure(Call<AreaResponse> call, Throwable t) {
                    callback.onFailureAreasResult(t.toString());
                }
            });
        }
    }

    @Override
    public void getAllIngredients(IngredientsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllIngredients("list").enqueue(new Callback<IngredientResponse>() {
                @Override
                public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                    if (response.isSuccessful()) {
                        ingredients = response.body().getIngredients();
                        callback.onSuccessIngredientsResult(ingredients);
                    }
                }

                @Override
                public void onFailure(Call<IngredientResponse> call, Throwable t) {

                    callback.onFailureIngredientsResult(t.toString());
                }
            });
        }
    }
}
