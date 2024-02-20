package com.mahmoud.myfoodplaner.plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.favourate.DeleteFavourateListener;
import com.mahmoud.myfoodplaner.home.MealClickListener;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import java.util.List;

public class NestedAdapterPlan extends RecyclerView.Adapter<NestedAdapterPlan.NestedViewHolder> {

    private List<LongMeal> mList;
    MealClickListener mealClickListener;
    DeleteFavourateListener deleteFavourateListener;

    public NestedAdapterPlan(List<LongMeal> mList, MealClickListener mealClickListener, DeleteFavourateListener deleteFavourateListener) {
        this.mealClickListener = mealClickListener;
        this.mList = mList;
        this.deleteFavourateListener = deleteFavourateListener;
    }
    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item_pro , parent , false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        holder.mealName.setText(mList.get(position).getStrMeal());
        Glide.with(holder.mealImage.getContext()).load(mList.get(position).getStrMealThumb()).into(holder.mealImage);
        holder.cardView.setOnClickListener(v -> mealClickListener.onMealClick(mList.get(position).getStrMeal()));
        holder.delete.setOnClickListener(v -> deleteFavourateListener.onDeleteFavourate(mList.get(position).getStrMeal()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{
        private TextView mealName;
        private ImageView mealImage;
        private CardView cardView;
        private ImageView delete;
        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.meal_name);
            mealImage = itemView.findViewById(R.id.meal_img);
            cardView = itemView.findViewById(R.id.cardView);
            delete = itemView.findViewById(R.id.close_icon);
        }
    }
}