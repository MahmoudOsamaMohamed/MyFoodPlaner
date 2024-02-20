package com.mahmoud.myfoodplaner.explore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.mahmoud.myfoodplaner.R;


import java.util.ArrayList;
import java.util.List;

public class MealAdpater extends ArrayAdapter<MealModel> {
    private List<MealModel> mealModelList;
    ClickMealSearchListener clickMealSearchListener;
    public MealAdpater(@NonNull Context context,@NonNull List<MealModel>mealModelList,ClickMealSearchListener clickMealSearchListener) {
        super(context, 0, mealModelList);
        this.mealModelList = new ArrayList<>(mealModelList);
        this.clickMealSearchListener = clickMealSearchListener;
        Log.i("exploreoo",this.mealModelList.size()+"");
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mealFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.meals_auto_complete, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        LinearLayout layout = convertView.findViewById(R.id.linear_layout);


        MealModel mealModel = getItem(position);

        if (mealModel != null) {
            textViewName.setText(mealModel.getName());
            Glide.with(getContext()).load(mealModel.getImage()).into(imageViewFlag);
        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
clickMealSearchListener.onMealClick(mealModel.getId());
            }
        });
        return convertView;
    }

    private Filter mealFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<MealModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(mealModelList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (MealModel item : mealModelList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((MealModel) resultValue).getName();
        }
    };
}
