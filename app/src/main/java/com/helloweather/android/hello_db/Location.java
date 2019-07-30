package com.helloweather.android.hello_db;

import org.litepal.crud.DataSupport;

public class Location extends DataSupport {
    private int id;
    private String LocationName;
    private int LocationCode;
    private int ProvinceId;

    public int getId() {
        return id;
    }

    public int getLocationCode() {
        return LocationCode;
    }

    public int getProvinceId() {
        return ProvinceId;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceId(int provinceId) {
        ProvinceId = provinceId;
    }

    public void setLocationCode(int locationCode) {
        LocationCode = locationCode;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }
}
