package com.mahmoud.myfoodplaner;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mahmoud.myfoodplaner.dbmodels.Area;
import com.mahmoud.myfoodplaner.dbmodels.CashingLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.Category;
import com.mahmoud.myfoodplaner.dbmodels.Ingerdiant;
import com.mahmoud.myfoodplaner.model.callbacks.AreasCallback;
import com.mahmoud.myfoodplaner.model.callbacks.CategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.DetailedCategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.IngredientsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.pojos.AreaPojo;
import com.mahmoud.myfoodplaner.model.pojos.DetailedCategory;
import com.mahmoud.myfoodplaner.model.pojos.Ingredient;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AreasCallback, DetailedCategoriesCallback, IngredientsCallback {
    BottomNavigationView bottomNavigationView;
    MealsRemoteDataSource mealsRemoteDataSource;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.setProperty("javax.net.debug", "ssl");
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);





        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        bottomNavigationView.setVisibility(View.VISIBLE);
//                    }
//                });
//
//            }
//        }).start();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeFragment) {
                navController.navigate(R.id.homeFragment);
            } else if (item.getItemId() == R.id.favourateFragment) {
                if(getSharedPreferences("user",MODE_PRIVATE)
                        .getString("email",null)!=null)
                navController.navigate(R.id.favourateFragment);
                else{

                    navController.navigate(R.id.signFragment);
                }
            } else if (item.getItemId() == R.id.planFragment) {
                if (getSharedPreferences("user",MODE_PRIVATE)
                        .getString("email",null)!=null)
                navController.navigate(R.id.planFragment);
                else{

                    navController.navigate(R.id.signFragment);
                }
            } else if (item.getItemId() == R.id.exploreFragment) {

                navController.navigate(R.id.exploreFragment);
            }
            else if (item.getItemId() == R.id.profileFragment) {
                if (getSharedPreferences("user",MODE_PRIVATE)
                        .getString("email",null)!=null)
                navController.navigate(R.id.profileFragment);
                else{

                    navController.navigate(R.id.signFragment);
                }
            }
            return true;
        });
        mealsRemoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        CashingLocalDataSource.getInstance(this).getAllAreas()
                .subscribeOn(Schedulers.io()).take(1).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<List<Area>>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<Area> areas) {
                        if(areas.size() == 0){
                            mealsRemoteDataSource.getAllAreas(MainActivity.this);
                            mealsRemoteDataSource.getDetailedCategories(MainActivity.this);
                            mealsRemoteDataSource.getAllIngredients(MainActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                 public void onComplete() {
//CashingLocalDataSource.getInstance(MainActivity.this).getAllIngerdiants().subscribeOn(Schedulers.io()).take(1)
//        .observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<Ingerdiant>>() {
//
//            @Override
//            public void onSubscribe(@NonNull Subscription s) {
//                s.request(1);
//            }
//
//            @Override
//            public void onNext(List<Ingerdiant> ingerdiants) {
//for(Ingerdiant ingerdiant:ingerdiants){
//
//}
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
                   }
                });



}

    @Override
    public void onSuccessAreasResult(List<AreaPojo> areaPojos) {
        for(AreaPojo areaPojo:areaPojos){
            Area area = new Area();

            area.name = areaPojo.getStrArea();
            CashingLocalDataSource.getInstance(this).insertArea(area).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe();
        }

    }

    @Override
    public void onFailureAreasResult(String error) {

    }



    @Override
    public void onSuccessIngredientsResult(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            Ingerdiant ingerdiant = new Ingerdiant();
            ingerdiant.name = ingredient.getStrIngredient();
            CashingLocalDataSource.getInstance(this).insertIngerdiant(ingerdiant).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe();
        }

    }

    @Override
    public void onFailureIngredientsResult(String error) {

    }

    @Override
    public void onSuccessDetailedCategoriesResult(List<DetailedCategory> categories) {
        for(DetailedCategory category:categories){
            Category category1 = new Category();
            category1.name = category.getStrCategory();
            category1.img_url = category.getStrCategoryThumb();
            category1.description = category.getStrCategoryDescription();
            CashingLocalDataSource.getInstance(this).insertCategory(category1).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    @Override
    public void onFailureDetailedCategoriesResult(String error) {

    }
    public static void hideBottomNav(){

    //    bottomNavigationView.setVisibility(View.GONE);
    }
    public static void showBottomNav(){

      //  bottomNavigationView.setVisibility(View.VISIBLE);
    }
}