package com.mahmoud.myfoodplaner.details;

import com.mahmoud.myfoodplaner.model.pojos.LongMeal;

public interface DetailsView  {
void showMealDetails(LongMeal longMeal);
void showMealDetailsError(String error);
}
