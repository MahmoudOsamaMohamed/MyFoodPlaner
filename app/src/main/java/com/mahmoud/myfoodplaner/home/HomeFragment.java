package com.mahmoud.myfoodplaner.home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.mahmoud.myfoodplaner.MainActivity;
import com.mahmoud.myfoodplaner.R;

import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.pojos.AreaPojo;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomeFragment extends Fragment implements HomeView,MealClickListener {
    LottieAnimationView animationViewLoading;
    LinearLayout animationViewNoInternet;
    ImageButton refresh;
    ScrollView scrollView;
    ImageView meal_img;
    SharedPreferences sharedPreferences;
    boolean isArea=false;
    MaterialCardView cardView;
    TextView meal_name;
    HomePresenter homePresenter;
    private RecyclerView recyclerView;
    private List<DataModel> mList;
    private List<DataModel>mListArea;
    private ItemAdapter adapter;
    private ItemAdapter adapterArea;
    int index = 0;
    String randomMealId;
    List<SimpleCategory>simpleCategoryList;
    List<List<ShortMeal>>shortMealsList;
    List<AreaPojo>areasList;
    private RecyclerView recyclerViewArea;


    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        mListArea = new ArrayList<>();
        shortMealsList = new ArrayList<>();
        areasList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation_view);
        navBar.setVisibility(View.VISIBLE);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Your custom back button behavior here
                // If you want to allow normal back button behavior, call the following line:
                // super.handleOnBackPressed();
            }
        });
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
MainActivity.showBottomNav();
        homePresenter = new HomePresenter(this, MealsRemoteDataSourceImpl.getInstance(),getActivity());
        meal_img = view.findViewById(R.id.meal_img);
        recyclerViewArea = view.findViewById(R.id.recyclerViewArea);
        meal_name = view.findViewById(R.id.meal_name);
        cardView = view.findViewById(R.id.meal_card);
        animationViewLoading = view.findViewById(R.id.loading);
        animationViewNoInternet = view.findViewById(R.id.noInternet);
        scrollView = view.findViewById(R.id.home_view);
        refresh=view.findViewById(R.id.refreshButton);
        cardView.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this).navigate(HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(randomMealId));

        });
        sharedPreferences = getActivity().getSharedPreferences("day", 0);

refresh.setOnClickListener(v->{
    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_self);
});

        if(!sharedPreferences.contains("lastRefreshDate")){
            long lastRefreshDate=sharedPreferences.getLong("lastRefreshDate",-1);
            Calendar lastRefreshCal = Calendar.getInstance();
            lastRefreshCal.setTimeInMillis(lastRefreshDate);

            Calendar todayCal = Calendar.getInstance();
            if(todayCal.get(Calendar.DAY_OF_YEAR) > lastRefreshCal.get(Calendar.DAY_OF_YEAR)
                || (todayCal.get(Calendar.YEAR) > lastRefreshCal.get(Calendar.YEAR))){
                homePresenter.getRandomMeal();
            }
        }
        else{
            randomMealId = sharedPreferences.getString("mealId","");
            meal_name.setText(sharedPreferences.getString("mealName",""));
            Glide.with(this).load(sharedPreferences.getString("mealThumb","")).into(meal_img);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewArea.setHasFixedSize(true);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterArea = new ItemAdapter(mListArea,this);
        recyclerViewArea.setAdapter(adapterArea);


        adapter = new ItemAdapter(mList,this);
        recyclerView.setAdapter(adapter);
/////////////////////////////////////////////////////////////////
//        homePresenter.getAreas();                           ///
        homePresenter.getAllCategories();                   ///
/////////////////////////////////////////////////////////////////
    }


    @Override
    public void showRandomMeal(List<LongMeal> longMeals) {
        meal_name.setText(longMeals.get(0).getStrMeal());
        Glide.with(this).load(longMeals.get(0).getStrMealThumb()).into(meal_img);
        randomMealId = longMeals.get(0).getIdMeal();
        sharedPreferences.edit()
                .putLong("lastRefreshDate", Calendar.getInstance().getTimeInMillis())
                .apply();
        sharedPreferences.edit().putString("mealId", randomMealId).apply();
        sharedPreferences.edit().putString("mealName", longMeals.get(0).getStrMeal()).apply();
        sharedPreferences.edit().putString("mealThumb", longMeals.get(0).getStrMealThumb()).apply();

    }

    @Override
    public void showRandomMealError(String error) {
        Log.i("random meal", error);
    }

    @Override
    public void setCategorieslist(List<SimpleCategory> categorieslist) {
        simpleCategoryList = categorieslist;

        for (SimpleCategory simpleCategory : simpleCategoryList) {
            homePresenter.getMealsByCategory(simpleCategory.getStrCategory());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Log.i("sout", simpleCategory.getStrCategory());
        }


    }

    @Override
    public void showCategoriesError(String error) {
        scrollView.setVisibility(View.GONE);
        animationViewLoading.setVisibility(View.GONE);
        animationViewNoInternet.setVisibility(View.VISIBLE);    }

    @Override
    public void updateCategoryBrowse(List<ShortMeal> shortMealsList) {
       if(!isArea){
        this.shortMealsList.add(shortMealsList);
        String output="";
        for(ShortMeal shortMeal:shortMealsList)
            output+=shortMeal.getStrMeal()+"   ||   ";
        Log.i("cout",output);
//        mList.get(index).setLongMealList(shortMealsList);
//      //  adapter.setList(shortMealsList);
//        index++;

        if(this.shortMealsList.size()==simpleCategoryList.size()) {
            for (int i = 0; i < simpleCategoryList.size(); i++) {
                mList.add(new DataModel(this.shortMealsList.get(i), simpleCategoryList.get(i).getStrCategory()));

            }
            Log.i("proplem", simpleCategoryList.toString());
//            Log.i("proplem",this.areasList.toString());


            adapter.notifyDataSetChanged();
            homePresenter.getAllAreas();
            this.shortMealsList.clear();
            isArea=true;
        }
//
//

        }
       else{
           this.shortMealsList.add(shortMealsList);
           if(this.shortMealsList.size()==areasList.size()){
               for (int i = 0; i < areasList.size(); i++) {
                   mListArea.add(new DataModel(this.shortMealsList.get(i), areasList.get(i).getStrArea()));
               }
               adapterArea.notifyDataSetChanged();
               scrollView.setVisibility(View.VISIBLE);
               animationViewLoading.setVisibility(View.GONE);
               isArea=false;
               this.shortMealsList.clear();

           }
       }


    }

    @Override
    public void showShortMealsError(String error) {
        scrollView.setVisibility(View.GONE);
        animationViewLoading.setVisibility(View.GONE);
        animationViewNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAreas(List<AreaPojo> areaPojos) {
        areasList = areaPojos;
        for(AreaPojo areaPojo:areaPojos){
            homePresenter.getMealsByArea(areaPojo.getStrArea());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void onMealClick(String id) {

        NavHostFragment.findNavController(HomeFragment.this).navigate(HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(id));

    }
}