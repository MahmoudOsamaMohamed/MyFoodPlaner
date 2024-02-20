package com.mahmoud.myfoodplaner.explore;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.MyViewHolder>{


    ClickMealSearchListener clickMealSearchListener;
    List<ShortMeal> mealsList;

    public List<ShortMeal> getMealsList() {
        return mealsList;
    }

    public void setMealsList(List<ShortMeal> mealsList) {
        this.mealsList = mealsList;
    }

    public ExploreAdapter(List<ShortMeal> productList, ClickMealSearchListener clickMealSearchListener) {
        this.mealsList = productList;
        this.clickMealSearchListener = clickMealSearchListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.meal_item_pro, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(listItem);
        Log.i("recycler", "onCreateViewHolder: ");
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        holder.meal_title.setText(mealsList.get(position).getStrMeal());
        holder.delete.setVisibility(View.GONE);

        Glide.with(holder.meal_img.getContext()).load(mealsList.get(position).getStrMealThumb()).into(holder.meal_img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favourate favourate=new Favourate();

                clickMealSearchListener.onMealClick(mealsList.get(holder.getAdapterPosition()).getIdMeal());
            }
        });
        // holder.my_img.setImageBitmap(mealsList.get(position).getBitmap());
        Log.i("recycler", "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return mealsList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
ImageView meal_img;
CardView cardView;
TextView meal_title;
ImageView delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            meal_img = itemView.findViewById(R.id.meal_img);
            cardView = itemView.findViewById(R.id.cardView);
            meal_title = itemView.findViewById(R.id.meal_name);
            delete = itemView.findViewById(R.id.close_icon);
        }
    }
}

