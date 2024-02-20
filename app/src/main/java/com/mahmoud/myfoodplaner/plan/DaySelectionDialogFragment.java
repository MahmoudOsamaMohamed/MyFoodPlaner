package com.mahmoud.myfoodplaner.plan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.mahmoud.myfoodplaner.R;

public class DaySelectionDialogFragment extends DialogFragment {

    public interface DaySelectionListener {
        void onDaySelected(String selectedDay);
    }

    private DaySelectionListener listener;

    public DaySelectionDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        try {
//            listener = (DaySelectionListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement DaySelectionListener");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_day_selection, container, false);

        Spinner daySpinner = view.findViewById(R.id.day_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.days_of_week,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        Button selectButton = view.findViewById(R.id.select_button);
        selectButton.setOnClickListener(v -> {
            String selectedDay = daySpinner.getSelectedItem().toString();
            listener.onDaySelected(selectedDay);
            dismiss();
        });

        return view;
    }
    public void setDaySelectionListener(DaySelectionListener listener) {
        this.listener = listener;
    }
}
