package com.mahmoud.myfoodplaner.favourate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.home.MealClickListener;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FavourateFragment extends Fragment implements FavourateView,DeleteFavourateListener, MealClickListener {
FavouratePresenter presenter;
RecyclerView rc;
LottieAnimationView show;
    List<ShortMeal> shortMeals;


    public FavourateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FavouratePresenter(this,getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rc = view.findViewById(R.id.rc);
        show=view.findViewById(R.id.empty);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rc.setLayoutManager(layoutManager);
        presenter.getAllFavourate();
    }

    @Override
    public void showAllFavourate(List<Favourate> favourates) {
       shortMeals = new ArrayList<>();
        for (Favourate favourate: favourates) {
            ShortMeal shortMeal = new ShortMeal();
            shortMeal.setIdMeal(favourate.id);
            shortMeal.setStrMeal(favourate.mealName);
            shortMeal.setStrMealThumb(favourate.img_url);
           shortMeals.add(shortMeal);
        }
        if (shortMeals.size() == 0) {
            show.setVisibility(View.VISIBLE);
            rc.setVisibility(View.GONE);
        }
        else{
            rc.setVisibility(View.VISIBLE);
            show.setVisibility(View.GONE);
        }
        FavAdapter adapter = new FavAdapter(shortMeals, this,this);
        rc.setAdapter(adapter);
    }

    @Override
    public void navigateWith(List<LongMeal> longMeals) {
        NavHostFragment.findNavController(this).navigate(FavourateFragmentDirections.actionFavourateFragmentToMealDetailsFragment(longMeals.get(0).getIdMeal()));
    }

    @Override
    public void onDeleteFavourate(String mealName) {
        presenter.deleteFavourate(mealName);
    }

    @Override
    public void onMealClick(String name) {
        presenter.getMealByName(name);
    }
}