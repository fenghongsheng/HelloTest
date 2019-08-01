package com.helloweather.android.hello_db;

import org.litepal.crud.DataSupport;

public class Location extends DataSupport {
    private int id;
    private String locationName;
    private int locationCode;
    private int provinceId;
    public int getId() {
        return id;
    }

    public int getLocationCode() {
        return locationCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void setLocationCode(int locationCode) {
        this.locationCode = locationCode;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
