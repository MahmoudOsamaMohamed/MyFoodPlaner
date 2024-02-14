package com.mahmoud.myfoodplaner.home;

import android.os.Bundle;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements HomeView,MealClickListener {
    ImageView meal_img;
    TextView meal_name;
    HomePresenter homePresenter;
    private RecyclerView recyclerView;
    private List<DataModel> mList;
    private ItemAdapter adapter;
    int index = 0;
    List<SimpleCategory>simpleCategoryList;
    List<List<ShortMeal>>shortMealsList;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        shortMealsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePresenter = new HomePresenter(this, MealsRemoteDataSourceImpl.getInstance());
        meal_img = view.findViewById(R.id.meal_img);
        meal_name = view.findViewById(R.id.meal_name);
        homePresenter.getRandomMeal();
        homePresenter.getAllCategories();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new ItemAdapter(mList,this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void showRandomMeal(List<LongMeal> longMeals) {
        meal_name.setText(longMeals.get(0).getStrMeal());
        Glide.with(this).load(longMeals.get(0).getStrMealThumb()).into(meal_img);
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
//        for(SimpleCategory simpleCategory:categorieslist) {
//            mList.add(new DataModel(new ArrayList<>(), simpleCategory.getStrCategory()));
//
//
//
//        }
//        for(SimpleCategory simpleCategory:categorieslist) {
//            homePresenter.getMealsByCategory(simpleCategory.getStrCategory());
//        }
//        adapter.notifyDataSetChanged();

    }

    @Override
    public void showCategoriesError(String error) {
        Log.i("categories", error);
    }

    @Override
    public void updateCategoryBrowse(List<ShortMeal> shortMealsList) {
        this.shortMealsList.add(shortMealsList);
        String output="";
        for(ShortMeal shortMeal:shortMealsList)
            output+=shortMeal.getStrMeal()+"   ||   ";
        Log.i("cout",output);
//        mList.get(index).setShortMealList(shortMealsList);
//      //  adapter.setList(shortMealsList);
//        index++;

        if(this.shortMealsList.size()==simpleCategoryList.size()){
            for(int i=0;i<simpleCategoryList.size();i++){
                mList.add(new DataModel(this.shortMealsList.get(i),simpleCategoryList.get(i).getStrCategory()));
            }
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showShortMealsError(String error) {
        Log.i("short meal", error);
    }

    @Override
    public void onMealClick(String id) {
        NavHostFragment.findNavController(HomeFragment.this).navigate(HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(id));

    }
}