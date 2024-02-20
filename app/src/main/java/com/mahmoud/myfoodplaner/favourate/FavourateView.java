package com.mahmoud.myfoodplaner.favourate;

import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;

import java.util.List;

public interface FavourateView {
    void showAllFavourate(List<Favourate> favourates);
    void navigateWith(List<LongMeal>longMeals);

}
