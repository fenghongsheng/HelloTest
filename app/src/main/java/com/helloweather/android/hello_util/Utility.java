package com.helloweather.android.hello_util;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.helloweather.android.hello_db.County;
import com.helloweather.android.hello_db.Location;
import com.helloweather.android.hello_db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject provinceObjec=jsonArray.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceCode(provinceObjec.getInt("id"));
                    province.setProvinceName(provinceObjec.getString("name"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleLocationResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject locationObject=jsonArray.getJSONObject(i);
                    Location location=new Location();
                    location.setLocationCode(locationObject.getInt("id"));
                    location.setLocationName(locationObject.getString("name"));
                    location.setProvinceId(provinceId);
                    location.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response,int locationId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject countyObject=jsonArray.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setLocationId(locationId);
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
