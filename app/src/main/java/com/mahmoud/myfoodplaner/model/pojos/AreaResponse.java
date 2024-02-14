package com.mahmoud.myfoodplaner.model.pojos;

import com.google.j2objc.annotations.Property;

import java.util.List;

public class AreaResponse {
    @Property("meals")
    private List<Area> areas;

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
