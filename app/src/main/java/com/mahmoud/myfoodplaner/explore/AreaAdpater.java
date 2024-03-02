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
import com.mahmoud.myfoodplaner.MainActivity;
import com.mahmoud.myfoodplaner.R;

import java.util.ArrayList;
import java.util.List;

public class AreaAdpater extends ArrayAdapter<AreaModel> {
    private List<AreaModel> areaModelList;
    ClickAreaSearchListener clickAreaSearchListener;
    public AreaAdpater(@NonNull Context context, @NonNull List<AreaModel> areaModelList, ClickAreaSearchListener clickAreaSearchListener) {
        super(context, 0, areaModelList);
        this.areaModelList = new ArrayList<>(areaModelList);
        this.clickAreaSearchListener = clickAreaSearchListener;
        Log.i("exploreoo",this.areaModelList.size()+"");
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


        AreaModel areModel = getItem(position);

        if (areModel != null) {
            textViewName.setText(areModel.getName());
            Glide.with(getContext()).load("https://flagcdn.com/56x42/"+
                    MainActivity.countryCodes.get(areModel.getName()).toLowerCase() +".png").into(imageViewFlag);

        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
clickAreaSearchListener.onAreaClick(areModel.getName());
            }
        });
        return convertView;
    }

    private Filter mealFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<AreaModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(areaModelList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (AreaModel item : areaModelList) {
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
            return ((AreaModel) resultValue).getName();
        }
    };
}
