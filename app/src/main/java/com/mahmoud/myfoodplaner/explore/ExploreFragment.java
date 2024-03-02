package com.mahmoud.myfoodplaner.explore;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.dbmodels.Area;
import com.mahmoud.myfoodplaner.dbmodels.CashingLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.Category;
import com.mahmoud.myfoodplaner.dbmodels.Ingerdiant;
import com.mahmoud.myfoodplaner.home.HomeFragment;
import com.mahmoud.myfoodplaner.homerefactor.ItemListner;
import com.mahmoud.myfoodplaner.homerefactor.Serizable;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ExploreFragment extends Fragment implements ShortMealsCallback,ClickMealSearchListener
        ,ClickAreaSearchListener ,ClickCategoryListener,ClickIngSearchListener{
    ImageView bottomsheet;
    RecyclerView recyclerView;
    AutoCompleteTextView autoCompleteTextView;
    List<MealModel> mealModelList;
    boolean isArea = false;
    boolean isCategory = false;
    boolean isIng = false;
    LinearLayout search;
    LinearLayout noInterent;
    ImageButton refresh;
    LottieAnimationView animationViewSearching;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomsheet = view.findViewById(R.id.filter);
        autoCompleteTextView = view.findViewById(R.id.search_bar);
        recyclerView = view.findViewById(R.id.recyclerView2);
        mealModelList = new ArrayList<>();
        animationViewSearching = view.findViewById(R.id.search_anim);
        search = view.findViewById(R.id.linearLayout);
        noInterent = view.findViewById(R.id.noInternet);
        refresh = view.findViewById(R.id.refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ExploreFragment.this)
                        .navigate(R.id.action_exploreFragment_self);
            }
        });
        if (!HomeFragment.isInternetAvailable(getActivity())) {
            search.setVisibility(View.GONE);
            noInterent.setVisibility(View.VISIBLE);
            animationViewSearching.setVisibility(View.GONE);
        } else {
            Serizable serizable = null;
            try {
                serizable = ExploreFragmentArgs.fromBundle(getArguments()).getItem();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (serizable != null) {
                animationViewSearching.setVisibility(View.GONE);
                //Snackbar.make(getView(), serizable.getName()+" from explore  "+serizable.getType(), Snackbar.LENGTH_LONG).show();
                switch (serizable.getType()) {
                    case ItemListner.AREA:
                        isArea = true;
                        MealsRemoteDataSourceImpl.getInstance().getAllMealsByArea(serizable.getName(), this);
                        recyclerView.requestFocus();

                        break;
                    case ItemListner.CATEGORY:
                        isCategory = true;
                        MealsRemoteDataSourceImpl.getInstance().getAllMealsByCategory(serizable.getName(), this);
                        recyclerView.requestFocus();


                        break;
                    case ItemListner.INGREDIANT:
                        isIng = true;
                        MealsRemoteDataSourceImpl.getInstance().getAllMealsByIngredient(serizable.getName(), this);
                        recyclerView.requestFocus();
                        break;
                }
            }
            else {

                CashingLocalDataSource.getInstance(getActivity()).getAllCategories().take(1).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<Category>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<Category> categories) {
                                for (Category category : categories) {
                                    MealsRemoteDataSourceImpl.getInstance().getAllMealsByCategory(category.name, ExploreFragment.this);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
            bottomsheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDialog();

                }
            });


        }
    }
    private void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout nameLayout = dialog.findViewById(R.id.layout_name);
        LinearLayout categoryLayout = dialog.findViewById(R.id.layout_category);
        LinearLayout countryLayout = dialog.findViewById(R.id.layout_country);
        LinearLayout ingredientLayout = dialog.findViewById(R.id.layout_ingredients);


        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                autoCompleteTextView.setAdapter(new MealAdpater(getActivity(),mealModelList,ExploreFragment.this));
                recyclerView.setAdapter(new ExploreAdapter( new ArrayList<>(),ExploreFragment.this));
                autoCompleteTextView.requestFocus();
            }
        });

        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                CashingLocalDataSource.getInstance(getActivity()).getAllCategories().take(1).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<Category>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
s.request(1);
                            }

                            @Override
                            public void onNext(List<Category> categories) {
                                List<CategoryModel> categoryList = new ArrayList<>();
                                for (Category category : categories) {
                                    categoryList.add(new CategoryModel(category.name,category.img_url));
                                }
                                Log.d("hahaha","onNext: "+categoryList.size());
                                CategoryAdpater categoryAdpater = new CategoryAdpater(getActivity(), categoryList,ExploreFragment.this);
                                autoCompleteTextView.setAdapter(categoryAdpater);
                                autoCompleteTextView.requestFocus();
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        });

        countryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
           //     Toast.makeText(getActivity(),"Upload is Clicked",Toast.LENGTH_SHORT).show();


                CashingLocalDataSource.getInstance(getActivity()).getAllAreas().take(1)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new FlowableSubscriber<List<Area>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<Area> areas) {
                                List<AreaModel> areaList = new ArrayList<>();
                                for (Area area : areas) {
                                    areaList.add(new AreaModel(area.name));
                                }
                                Log.d("hahaha","onNext: "+areaList.size());
                                AreaAdpater areaAdpater = new AreaAdpater(getActivity(), areaList,ExploreFragment.this);
                                autoCompleteTextView.setAdapter(areaAdpater);
                                autoCompleteTextView.requestFocus();
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                Toast.makeText(getActivity(),"Edit is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        ingredientLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            CashingLocalDataSource.getInstance(getActivity()).getAllIngerdiants().take(1)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new FlowableSubscriber<List<Ingerdiant>>() {

                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                            s.request(1);
                        }

                        @Override
                        public void onNext(List<Ingerdiant> ingerdiants) {
List<IngModel> ingerdiantModelList = new ArrayList<>();
for (Ingerdiant ingerdiant:ingerdiants){
    ingerdiantModelList.add(new IngModel(ingerdiant.name));
}
                            Log.d("hahaha","onNext: "+ingerdiantModelList.size());
                            IngAdpater ingAdapter = new IngAdpater(getActivity(), ingerdiantModelList,ExploreFragment.this);
                            autoCompleteTextView.setAdapter(ingAdapter);
                            autoCompleteTextView.requestFocus();

                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    @Override
    public void onSuccessShortMealsResult(List<ShortMeal> meals) {
        if (isArea) {
            isArea = false;
            ExploreAdapter exploreAdapter = new ExploreAdapter(meals, ExploreFragment.this);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
            recyclerView.setAdapter(exploreAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        else if(isCategory){
            isCategory = false;
            ExploreAdapter exploreAdapter = new ExploreAdapter(meals, ExploreFragment.this);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);            recyclerView.setAdapter(exploreAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);

        }
        else if(isIng){
            isIng = false;
            ExploreAdapter exploreAdapter = new ExploreAdapter(meals, ExploreFragment.this);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);            recyclerView.setAdapter(exploreAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        else {
            Log.i("explore", meals.size() + "");

            for (ShortMeal shortMeal : meals) {
                MealModel mealModel = new MealModel(shortMeal.getStrMeal(), shortMeal.getStrMealThumb(), shortMeal.getIdMeal());
                mealModelList.add(mealModel);
            }
            Log.i("explore", mealModelList.size() + "ex" + mealModelList.get(0).getName());
            MealAdpater mealAdpater = new MealAdpater(getActivity(), mealModelList, this);
            autoCompleteTextView.setAdapter(mealAdpater);
        }
    }
    @Override
    public void onFailureShortMealsResult(String error) {
        Log.i("explore",error);

    }

    @Override
    public void onMealClick(String id) {
        NavHostFragment.findNavController(this).navigate(ExploreFragmentDirections.actionExploreFragmentToMealDetailsFragment(id));
    }

    @Override
    public void onAreaClick(String name) {
        animationViewSearching.setVisibility(View.GONE);
        isArea=true;
        MealsRemoteDataSourceImpl.getInstance().getAllMealsByArea(name,this);
        recyclerView.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);

    }

    @Override
    public void onCategoryClick(String name) {
        animationViewSearching.setVisibility(View.GONE);
isCategory=true;
        MealsRemoteDataSourceImpl.getInstance().getAllMealsByCategory(name,this);
        recyclerView.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }

    @Override
    public void onIngClick(String name) {
        animationViewSearching.setVisibility(View.GONE);
        isIng=true;
        MealsRemoteDataSourceImpl.getInstance().getAllMealsByIngredient(name,this);
        recyclerView.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }
}