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

public class IngAdpater extends ArrayAdapter<IngModel> {
    private List<IngModel> ingModelList;
    ClickIngSearchListener clickIngSearchListener;
    public IngAdpater(@NonNull Context context, @NonNull List<IngModel> ingModelList, ClickIngSearchListener clickAreaSearchListener) {
        super(context, 0, ingModelList);
        this.ingModelList = new ArrayList<>(ingModelList);
        this.clickIngSearchListener = clickAreaSearchListener;
        Log.i("exploreoo",this.ingModelList.size()+"");
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


        IngModel areModel = getItem(position);

        if (areModel != null) {
            textViewName.setText(areModel.getName());
            Glide.with(getContext()).load("https://www.themealdb.com/images/ingredients/"+areModel.getName()+"-Small.png").into(imageViewFlag);

        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
clickIngSearchListener.onIngClick(areModel.getName());
            }
        });
        return convertView;
    }

    private Filter mealFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<IngModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(ingModelList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (IngModel item : ingModelList) {
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
            return ((IngModel) resultValue).getName();
        }
    };
}
