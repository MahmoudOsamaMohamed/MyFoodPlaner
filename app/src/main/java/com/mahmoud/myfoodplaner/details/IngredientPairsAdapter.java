package com.mahmoud.myfoodplaner.details;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.myfoodplaner.R;

import java.util.List;
import java.util.zip.Inflater;

public class IngredientPairsAdapter extends RecyclerView.Adapter<IngredientPairsAdapter.ViewHolder> {
    private Pair<List<String>, List<String>> ingredientPairs;

    public IngredientPairsAdapter(Pair<List<String>, List<String>> ingredientPairs) {
        this.ingredientPairs = ingredientPairs;
    }

    @NonNull
    @Override
    public IngredientPairsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientPairsAdapter.ViewHolder holder, int position) {
        Log.i("bind view", ingredientPairs.first.get(position)+" "+ingredientPairs.second.get(position));
        holder.ingedientName.setText(ingredientPairs.first.get(position));
        holder.quantity.setText(ingredientPairs.second.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientPairs.first.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingedientName, quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingedientName = itemView.findViewById(R.id.ingredientName);
            quantity = itemView.findViewById(R.id.messure);
        }
    }
}
