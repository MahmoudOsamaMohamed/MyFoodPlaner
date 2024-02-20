package com.mahmoud.myfoodplaner.model;

import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.dbmodels.table.table;

import java.util.List;

public class FireBaseModel {
    public List<table> getPlans() {
        return plans;
    }

    public FireBaseModel(List<table> plans, List<Favourate> favourates) {
        this.plans = plans;
        this.favourates = favourates;
    }

    public void setPlans(List<table> plans) {
        this.plans = plans;
    }

    public List<Favourate> getFavourates() {
        return favourates;
    }

    public void setFavourates(List<Favourate> favourates) {
        this.favourates = favourates;
    }

    List<table>plans;
    List<Favourate>favourates;
}
