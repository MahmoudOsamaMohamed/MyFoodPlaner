package com.mahmoud.myfoodplaner.details;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.myfoodplaner.R;

import java.util.List;

public class MealStepsAdapter extends RecyclerView.Adapter<MealStepsAdapter.ViewHolder> {
    private List<String> steps;

    public MealStepsAdapter(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String step = steps.get(position);

        holder.stepNumberTextView.setText("Step " + (position + 1));
        holder.stepDescriptionTextView.setText(step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepNumberTextView;
        TextView stepDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            stepNumberTextView = itemView.findViewById(R.id.stepNumberTextView);
            stepDescriptionTextView = itemView.findViewById(R.id.stepDescriptionTextView);
        }
    }
}
