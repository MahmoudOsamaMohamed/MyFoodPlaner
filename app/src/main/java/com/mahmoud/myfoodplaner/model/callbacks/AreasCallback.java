package com.mahmoud.myfoodplaner.model.callbacks;

import com.mahmoud.myfoodplaner.model.pojos.AreaPojo;

import java.util.List;

public interface AreasCallback {

    void onSuccessAreasResult(List<AreaPojo> areaPojos);
    void onFailureAreasResult(String error);
}
