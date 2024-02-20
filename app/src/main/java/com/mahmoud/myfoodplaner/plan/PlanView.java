package com.mahmoud.myfoodplaner.plan;


import com.mahmoud.myfoodplaner.dbmodels.table.table;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;

import java.util.List;

public interface PlanView {
    public void showDayMeals(List<table> shortMealsList);
    public void showMeal(List<LongMeal>longMeals);
}
