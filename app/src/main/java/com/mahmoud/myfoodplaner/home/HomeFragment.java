package com.mahmoud.myfoodplaner.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Network;
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
import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.myfoodplaner.MainActivity;
import com.mahmoud.myfoodplaner.R;

import com.mahmoud.myfoodplaner.dbmodels.Area;
import com.mahmoud.myfoodplaner.dbmodels.Category;
import com.mahmoud.myfoodplaner.dbmodels.Ingerdiant;
import com.mahmoud.myfoodplaner.homerefactor.HomeAdapter;
import com.mahmoud.myfoodplaner.homerefactor.HomeItem;
import com.mahmoud.myfoodplaner.homerefactor.ItemListner;
import com.mahmoud.myfoodplaner.homerefactor.Serizable;
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
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class HomeFragment extends Fragment implements HomeView,MealClickListener, ItemListner {
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
    private RecyclerView recylerViewIng;


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
        homePresenter = new HomePresenter(this, MealsRemoteDataSourceImpl.getInstance(), getActivity());
        meal_img = view.findViewById(R.id.meal_img);
        recyclerViewArea = view.findViewById(R.id.recyclerViewArea);
        meal_name = view.findViewById(R.id.meal_name);
        cardView = view.findViewById(R.id.meal_card);
        animationViewLoading = view.findViewById(R.id.loading);
        animationViewNoInternet = view.findViewById(R.id.noInternet);
        scrollView = view.findViewById(R.id.home_view);
        recylerViewIng = view.findViewById(R.id.recyclerViewIngredient);
        refresh = view.findViewById(R.id.refreshButton);

        cardView.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this).navigate(HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(randomMealId));

        });
        sharedPreferences = getActivity().getSharedPreferences("day", 0);

        refresh.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_self);
        });
        if (!isInternetAvailable(getActivity())) {
            scrollView.setVisibility(View.GONE);
            animationViewLoading.setVisibility(View.GONE);
            animationViewNoInternet.setVisibility(View.VISIBLE);
        }
        else {
            new Thread(() -> {

                try {
                    Thread.sleep(1000);
                    getActivity().runOnUiThread(() -> {
                        scrollView.setVisibility(View.VISIBLE);
                        animationViewLoading.setVisibility(View.GONE);
                        animationViewNoInternet.setVisibility(View.GONE);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            MainActivity.showBottomNav();


            if (!sharedPreferences.contains("lastRefreshDate")) {
                long lastRefreshDate = sharedPreferences.getLong("lastRefreshDate", -1);
                Calendar lastRefreshCal = Calendar.getInstance();
                lastRefreshCal.setTimeInMillis(lastRefreshDate);

                Calendar todayCal = Calendar.getInstance();
                if (todayCal.get(Calendar.DAY_OF_YEAR) > lastRefreshCal.get(Calendar.DAY_OF_YEAR)
                        || (todayCal.get(Calendar.YEAR) > lastRefreshCal.get(Calendar.YEAR))) {
                    homePresenter.getRandomMeal();
                }
            } else {
                randomMealId = sharedPreferences.getString("mealId", "");
                meal_name.setText(sharedPreferences.getString("mealName", ""));
                Glide.with(this).load(sharedPreferences.getString("mealThumb", "")).into(meal_img);
            }

            recyclerView = view.findViewById(R.id.recyclerView);


/////////////////////////////////////////////////////////////////
//        homePresenter.getAreas();                           ///
            homePresenter.getAllCategories();
            homePresenter.getAllIngredients();
            homePresenter.getAllAreas();

/////////////////////////////////////////////////////////////////
        }

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
    public void showCategories(List<Category> simpleCategories) {
        List<HomeItem>categoriesItems = new ArrayList<>();
        for(Category category:simpleCategories){
            categoriesItems.add(new HomeItem(category.img_url,category.name,category.description,CATEGORY));
        }
        HomeAdapter homeAdapter = new HomeAdapter(categoriesItems, HomeFragment.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(homeAdapter);


    }

    @Override
    public void showCategoriesError(String error) {

    }

    @Override
    public void showAreas(List<Area> areas) {
        List<HomeItem>areasItems = new ArrayList<>();
        for(Area area:areas){
            areasItems.add(new HomeItem(
                    "https://flagcdn.com/h120/" +MainActivity.countryCodes.get(area.name).toLowerCase()+ ".png"
                    ,area.name,MainActivity.countryCuisines.get(area.name),AREA));
        }
        HomeAdapter homeAdapter = new HomeAdapter(areasItems, HomeFragment.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewArea.setLayoutManager(layoutManager);
        recyclerViewArea.setAdapter(homeAdapter);

    }

    @Override
    public void showAreasError(String error) {

    }

    @Override
    public void showIngrediants(List<Ingerdiant> ingerdiants) {
        List<HomeItem>ingerdiantsItems = new ArrayList<>();

        List<Ingerdiant>myIngs=ingerdiants.subList(0,25);
        for(Ingerdiant ingerdiant:myIngs){
            ingerdiantsItems.add(new HomeItem(
                    "https://www.themealdb.com/images/ingredients/"+ingerdiant.name+"-Small.png"
                    ,ingerdiant.name,MainActivity.ingredientDescriptions.get(ingerdiant.name),INGREDIANT));
        }
        HomeAdapter homeAdapter = new HomeAdapter(ingerdiantsItems, HomeFragment.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recylerViewIng.setLayoutManager(layoutManager);
        recylerViewIng.setAdapter(homeAdapter);
    }

    @Override
    public void showIngrediantsError(String error) {

    }


//    @Override
//    public void showCategoriesError(String error) {
//        scrollView.setVisibility(View.GONE);
//        animationViewLoading.setVisibility(View.GONE);
//        animationViewNoInternet.setVisibility(View.VISIBLE);    }



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

    @Override
    public void onItemClicked(String name, int type) {
        NavHostFragment.findNavController(HomeFragment.this).
                navigate(HomeFragmentDirections.actionHomeFragmentToExploreFragment(new Serizable(name,type)));
       // Snackbar.make(getView(), "onItemClicked " + name + " " + type, Snackbar.LENGTH_LONG).show();
    }
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                // For SDK version 23 and above
                Network activeNetwork = connectivityManager.getActiveNetwork();
                if (activeNetwork != null) {
                    return true;
                }
            } else {
                // For SDK version below 23
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}