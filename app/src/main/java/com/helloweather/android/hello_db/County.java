package com.helloweather.android.hello_db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private int LocationId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

    public int getLocationId() {
        return LocationId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherId() {
        return weatherId;
    }
}
