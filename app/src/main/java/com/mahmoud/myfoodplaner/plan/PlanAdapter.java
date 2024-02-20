package com.mahmoud.myfoodplaner.plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.favourate.DeleteFavourateListener;
import com.mahmoud.myfoodplaner.home.MealClickListener;
import com.mahmoud.myfoodplaner.home.NestedAdapter;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ItemViewHolder> {

    private List<PlanModel> mList;
    private List<LongMeal> list = new ArrayList<>();
    NestedAdapterPlan adapter;
    MealClickListener mealClickListener;
    AddClickListener addClickListener;
    DeleteFavourateListener deleteFavourateListener;

    public void setList(List<LongMeal> list) {
        this.list = list;
    }

    public PlanAdapter(List<PlanModel> mList, MealClickListener mealClickListener, AddClickListener addClickListener,DeleteFavourateListener deleteFavourateListener) {
        this.mealClickListener = mealClickListener;
        this.mList  = mList;
        this.addClickListener = addClickListener;
        this.deleteFavourateListener=deleteFavourateListener;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        PlanModel model = mList.get(position);
        holder.mTextView.setText(model.getDay());
holder.add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        addClickListener.onAddClick(position);
    }
});

        List<LongMeal> longMealList = model.getLongMealList();
        adapter = new NestedAdapterPlan(longMealList, mealClickListener,deleteFavourateListener);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                list = model.getLongMealList();
//
//                notifyItemChanged(holder.getAdapterPosition());
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
       private LinearLayout linearLayout;
//        private RelativeLayout expandableLayout;
        private TextView mTextView;
        Button add;
        private RecyclerView nestedRecyclerView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

//            linearLayout = itemView.findViewById(R.id.linear_layout);
//            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mTextView = itemView.findViewById(R.id.day);
            add= itemView.findViewById(R.id.add);
            nestedRecyclerView = itemView.findViewById(R.id.plan_meals_ryc);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}