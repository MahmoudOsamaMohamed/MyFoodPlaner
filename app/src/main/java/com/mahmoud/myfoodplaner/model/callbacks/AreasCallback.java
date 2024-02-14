package com.mahmoud.myfoodplaner.model.callbacks;

import com.mahmoud.myfoodplaner.model.pojos.Area;

import java.util.List;

public interface AreasCallback {

    void onSuccessAreasResult(List<Area> areas);
    void onFailureAreasResult(String error);
}
