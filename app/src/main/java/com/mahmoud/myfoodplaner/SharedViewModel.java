package com.mahmoud.myfoodplaner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> bottomNavVisibility = new MutableLiveData<>();

    public LiveData<Integer> getBottomNavVisibility() {
        return bottomNavVisibility;
    }

    public void setBottomNavVisibility(int visibility) {
        bottomNavVisibility.setValue(visibility);
    }
}
