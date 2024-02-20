package com.mahmoud.myfoodplaner.model;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import com.mahmoud.myfoodplaner.model.callbacks.AreasCallback;
import com.mahmoud.myfoodplaner.model.callbacks.CategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.DetailedCategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.IngredientsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.LongMealsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.*;
;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
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
    private List<AreaPojo> areaPojos;
    private List<DetailedCategory> detailedCategories;

    private MealsRemoteDataSourceImpl() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
            foodApi.getAllMealsByCategory(category).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ShortMealResponse>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull ShortMealResponse shortMealResponse) {

                    shortMeals = shortMealResponse.getShortMeals();


                }

                @Override
                public void onError(@NonNull Throwable e) {

                    callback.onFailureShortMealsResult(e.toString());
                }

                @Override
                public void onComplete() {
                    callback.onSuccessShortMealsResult(shortMeals);
                }
            });
            /*foodApi.getAllMealsByCategory(category).enqueue(new Callback<ShortMealResponse>() {
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
            });*/
        }
    }

    @Override
    public void getAllCategories(CategoriesCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllCategories("list")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SimpleCategoryResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull SimpleCategoryResponse simpleCategoryResponse) {

                            categories = simpleCategoryResponse.getSimpleCategoryList().stream()
                                    .filter(simpleCategory -> !simpleCategory.getStrCategory().equals("Pork"))
                                    .collect(toList());
                            callback.onSuccessCategoriesResult(categories);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                            callback.onFailureCategoriesResult(e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

            /*foodApi.getAllCategories("list").enqueue(new Callback<SimpleCategoryResponse>() {
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
            });*/
        }
    }

    @Override
    public void getMealsByName(String name, LongMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getMealsByName(name).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LongMealResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull LongMealResponse longMealResponse) {

                            longMeals = longMealResponse.getMeals();
                            callback.onSuccessLongMealsResult(longMeals);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                            callback.onFailureLongMealsResult(e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            /*foodApi.getMealsByName(name).enqueue(new Callback<LongMealResponse>() {
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
            });*/
        }
    }

    @Override
    public void getMealsByID(String id, LongMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getMealsByID(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LongMealResponse>() {

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull LongMealResponse longMealResponse) {

                            longMeals = longMealResponse.getMeals();
                            callback.onSuccessLongMealsResult(longMeals);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                            callback.onFailureLongMealsResult(e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
          /*foodApi.getMealsByID(id).enqueue(new Callback<LongMealResponse>() {
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
            });*/
        }
    }

    @Override
    public void getAllMealsByIngredient(String ingredient, ShortMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
       if (foodApi != null) {
           foodApi.getAllMealsByIngredient(ingredient).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ShortMealResponse>() {

                       @Override
                       public void onSubscribe(@NonNull Disposable d) {

                       }

                       @Override
                       public void onNext(@NonNull ShortMealResponse shortMealResponse) {

                           shortMeals = shortMealResponse.getShortMeals();
                           callback.onSuccessShortMealsResult(shortMeals);
                       }

                       @Override
                       public void onError(@NonNull Throwable e) {

                           callback.onFailureShortMealsResult(e.toString());
                       }

                       @Override
                       public void onComplete() {

                       }
                   });
//            foodApi.getAllMealsByIngredient(ingredient).enqueue(new Callback<ShortMealResponse>() {
//                @Override
//                public void onResponse(Call<ShortMealResponse> call, Response<ShortMealResponse> response) {
//                    if (response.isSuccessful()) {
//                        shortMeals = response.body().getShortMeals();
//                        callback.onSuccessShortMealsResult(shortMeals);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ShortMealResponse> call, Throwable t) {
//
//                    callback.onFailureShortMealsResult(t.toString());
//                }
//            });
        }
    }

    @Override
    public void getAllMealsByArea(String area, ShortMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllMealsByArea(area).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ShortMealResponse>() {

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull ShortMealResponse shortMealResponse) {

                    shortMeals = shortMealResponse.getShortMeals();
                    callback.onSuccessShortMealsResult(shortMeals);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                    callback.onFailureShortMealsResult(e.toString());
                }

                @Override
                public void onComplete() {

                }
            });
//            foodApi.getAllMealsByArea(area).enqueue(new Callback<ShortMealResponse>() {
//                @Override
//                public void onResponse(Call<ShortMealResponse> call, Response<ShortMealResponse> response) {
//                    if (response.isSuccessful()) {
//                        shortMeals = response.body().getShortMeals();
//                        callback.onSuccessShortMealsResult(shortMeals);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ShortMealResponse> call, Throwable t) {
//
//                    callback.onFailureShortMealsResult(t.toString());
//                }
//            });
        }
    }

    @Override
    public void getRandomMeal(LongMealsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getRandomMeal().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LongMealResponse>() {

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull LongMealResponse longMealResponse) {

                    longMeals = longMealResponse.getMeals();
                    callback.onSuccessLongMealsResult(longMeals);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                    callback.onFailureLongMealsResult(e.toString());
                }

                @Override
                public void onComplete() {

                }
            });
//            foodApi.getRandomMeal().enqueue(new Callback<LongMealResponse>() {
//                @Override
//                public void onResponse(Call<LongMealResponse> call, Response<LongMealResponse> response) {
//                    if (response.isSuccessful()) {
//                        longMeals = response.body().getMeals();
//                        callback.onSuccessLongMealsResult(longMeals);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<LongMealResponse> call, Throwable t) {
//                    Log.i("meals", t.toString());
//                    callback.onFailureLongMealsResult(t.toString());
//                }
//            });
        }
    }

    @Override
    public void getAllAreas(AreasCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllAreas("list").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AreaResponse>() {

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull AreaResponse areaResponse) {

                    areaPojos = areaResponse.getAreas();



                }

                @Override
                public void onError(@NonNull Throwable e) {

                    callback.onFailureAreasResult(e.toString());
                }

                @Override
                public void onComplete() {
                    Log.i("areas", areaPojos.get(0).getStrArea());
                    callback.onSuccessAreasResult(areaPojos);
                }
            });
//            foodApi.getAllAreas("list").enqueue(new Callback<AreaResponse>() {
//                @Override
//                public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
//                    if (response.isSuccessful()) {
//                        areas = response.body().getAreas();
//                        callback.onSuccessAreasResult(areas);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AreaResponse> call, Throwable t) {
//                    callback.onFailureAreasResult(t.toString());
//                }
//            });
        }
    }

    @Override
    public void getAllIngredients(IngredientsCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getAllIngredients("list").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<IngredientResponse>() {

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull IngredientResponse ingredientResponse) {

                    ingredients = ingredientResponse.getIngredients();

                }

                @Override
                public void onError(@NonNull Throwable e) {

                    callback.onFailureIngredientsResult(e.toString());
                }

                @Override
                public void onComplete() {
                    callback.onSuccessIngredientsResult(ingredients);
                }
            });
//            foodApi.getAllIngredients("list").enqueue(new Callback<IngredientResponse>() {
//                @Override
//                public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
//                    if (response.isSuccessful()) {
//                        ingredients = response.body().getIngredients();
//                        callback.onSuccessIngredientsResult(ingredients);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<IngredientResponse> call, Throwable t) {
//
//                    callback.onFailureIngredientsResult(t.toString());
//                }
//            });
        }
    }

    @Override
    public void getDetailedCategories(DetailedCategoriesCallback callback) {
        foodApi = retrofit.create(FoodApi.class);
        if (foodApi != null) {
            foodApi.getDetailedCategories().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DetailedCategoryResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull DetailedCategoryResponse detailedCategoryResponse) {

                            detailedCategories = detailedCategoryResponse.getCategories();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                            callback.onSuccessDetailedCategoriesResult(detailedCategories);
                        }
                    });
        }
    }
}