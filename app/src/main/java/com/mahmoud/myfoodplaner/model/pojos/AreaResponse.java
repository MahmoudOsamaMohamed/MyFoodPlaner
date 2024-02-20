package com.mahmoud.myfoodplaner.model.pojos;

import com.google.gson.annotations.SerializedName;
import com.google.j2objc.annotations.Property;

import java.util.List;

public class AreaResponse {
    @SerializedName("meals")
    private List<AreaPojo> areaPojos;

    public List<AreaPojo> getAreas() {
        return areaPojos;
    }

    public void setAreas(List<AreaPojo> areaPojos) {
        this.areaPojos = areaPojos;
    }
}
