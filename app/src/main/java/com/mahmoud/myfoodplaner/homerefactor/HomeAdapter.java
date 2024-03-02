package com.mahmoud.myfoodplaner.homerefactor;

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
import com.mahmoud.myfoodplaner.home.ItemAdapter;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private List<HomeItem> itemList;
    private ItemListner itemListner;

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageView;
        public TextView nameTextView;
        public TextView descriptionTextView;

        public HomeViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.item_img);
            nameTextView = itemView.findViewById(R.id.item_name);
            descriptionTextView = itemView.findViewById(R.id.item_description);
        }
    }

    public HomeAdapter(List<HomeItem> itemList, ItemListner itemListner) {
        this.itemListner = itemListner;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new HomeViewHolder(v);
    }



    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        HomeItem currentItem = itemList.get(position);

        Glide.with(holder.imageView.getContext()).load(currentItem.getImageUrl()).into(holder.imageView);
        holder.nameTextView.setText(currentItem.getName());
        holder.descriptionTextView.setText(currentItem.getDescription());

        // Set click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListner.onItemClicked(holder.nameTextView.getText().toString(),currentItem.getType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
