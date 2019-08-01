package com.helloweather.android.hello_gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("cid")
    public String weatherID;
    @SerializedName("location")
    public String cityName;
}
