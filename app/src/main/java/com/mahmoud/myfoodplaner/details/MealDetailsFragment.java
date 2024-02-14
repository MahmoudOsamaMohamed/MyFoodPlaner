package com.mahmoud.myfoodplaner.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Arrays;


public class MealDetailsFragment extends Fragment implements DetailsView {
YouTubePlayerView youtube_player_view;
ImageView meal_image;
TextView meal_name, meal_category, meal_area, meal_description;
RecyclerView stepsRecyclerView;
TextView stepsTv;
RecyclerView ingredientsRecyclerView;
DetailsPresenter detailsPresenter;
    public MealDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsPresenter = new DetailsPresenter(this, MealsRemoteDataSourceImpl.getInstance());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        youtube_player_view=view.findViewById(R.id.youtube_player_view);
        detailsPresenter.getMealDetails(MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId());
        meal_image=view.findViewById(R.id.meal_img);
        meal_name=view.findViewById(R.id.meal_name);
        meal_category=view.findViewById(R.id.meal_category);
        meal_area=view.findViewById(R.id.meal_area);
        meal_description=view.findViewById(R.id.meal_description);
        stepsRecyclerView=view.findViewById(R.id.steps_recycler_view);
        stepsTv=view.findViewById(R.id.steps_tv);
        ingredientsRecyclerView=view.findViewById(R.id.ingredinets_recycler_view);

    }

    @Override
    public void showMealDetails(LongMeal longMeal) {
        if (longMeal.getStrMeal()!=null)
            meal_name.setText(longMeal.getStrMeal());
        if (longMeal.getStrCategory()!=null)
            meal_category.setText(longMeal.getStrCategory());
        if (longMeal.getStrArea()!=null)
            meal_area.setText(longMeal.getStrArea());

        if (longMeal.getStrMealThumb()!=null)
            Glide.with(getContext()).load(longMeal.getStrMealThumb()).into(meal_image);
        if (longMeal.getStrInstructions().contains("\n")){
            meal_description.setVisibility(View.GONE);
            stepsTv.setVisibility(View.VISIBLE);
            stepsRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            stepsRecyclerView.setLayoutManager(layoutManager);
            MealStepsAdapter mealStepsAdapter = new MealStepsAdapter(Arrays.asList(longMeal.getStrInstructions().split("\r\n")));
            stepsRecyclerView.setAdapter(mealStepsAdapter);
        }
        else {
            meal_description.setText(longMeal.getStrInstructions());
            meal_description.setVisibility(View.VISIBLE);
            stepsTv.setVisibility(View.GONE);
            stepsRecyclerView.setVisibility(View.GONE);

        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        Log.i("ingredient",longMeal.getStrIngredient().toString());
        IngredientPairsAdapter ingredientPairsAdapter= new IngredientPairsAdapter(longMeal.getStrIngredient());
        ingredientsRecyclerView.setAdapter(ingredientPairsAdapter);
        youtube_player_view.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                try {
                    youTubePlayer.loadVideo(longMeal.getStrYoutube().substring(32),0);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void showMealDetailsError(String error) {

    }
}