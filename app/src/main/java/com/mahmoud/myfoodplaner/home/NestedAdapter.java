package com.mahmoud.myfoodplaner.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import java.util.List;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.NestedViewHolder> {

    private List<ShortMeal> mList;
    MealClickListener mealClickListener;

    public NestedAdapter(List<ShortMeal> mList, MealClickListener mealClickListener) {
        this.mealClickListener = mealClickListener;
        this.mList = mList;
    }
    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child , parent , false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        holder.mealName.setText(mList.get(position).getStrMeal());
        Glide.with(holder.mealImage.getContext()).load(mList.get(position).getStrMealThumb()).into(holder.mealImage);
        holder.cardView.setOnClickListener(v -> mealClickListener.onMealClick(mList.get(position).getIdMeal()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{
        private TextView mealName;
        private ImageView mealImage;
        private MaterialCardView cardView;
        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.meal_name);
            mealImage = itemView.findViewById(R.id.meal_img);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}