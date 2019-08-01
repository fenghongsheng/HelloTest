package com.helloweather.android.hello_gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    //public String cond_code;
    public String cond_txt;
    @SerializedName("tmp")
    public String tmperature;
}
