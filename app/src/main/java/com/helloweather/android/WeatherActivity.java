package com.helloweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.helloweather.android.hello_gson.Forecast;
import com.helloweather.android.hello_gson.Lifestyle;
import com.helloweather.android.hello_gson.Weather;
import com.helloweather.android.hello_util.HttpUtil;
import com.helloweather.android.hello_util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    ImageView bingPicImg;
    public DrawerLayout drawerLayout;
    private Button backHome;
    private ScrollView weatherLayout;
    private TextView titleLocation;
    private TextView titleUpdatTime;
    private TextView weatherDegreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView typeText;
    private TextView brfText;
    private TextView txtText;
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        swipeRefresh=findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        weatherLayout=findViewById(R.id.weather_layout);
        titleLocation=findViewById(R.id.title_location);
        titleUpdatTime=findViewById(R.id.title_update_time);
        weatherDegreeText=findViewById(R.id.weather_degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        typeText = findViewById(R.id.type_text);
        brfText = findViewById(R.id.brf_text);
        txtText = findViewById(R.id.txt_text);
        bingPicImg = findViewById(R.id.bing_pic_img);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        String bingPic = prefs.getString("bing_pic", null);
        drawerLayout=findViewById(R.id.drawer_layout);
        backHome=findViewById(R.id.back_home);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        if(weatherString!=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            mWeatherId=weather.basic.weatherID;
            showWeatherInfo(weather);
        }else {
            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }
    public void requestWeather(final String weatherId){
        String weatherUrl = "https://free-api.heweather.net/s6/weather?location=" + weatherId + "&key=bc6f94a766604780abe16c52b3205c85";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"奥特曼找不到了",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    final String responseText=response.body().string();
                    final Weather weather=Utility.handleWeatherResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(weather!=null && "ok".equals(weather.status)){
                                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                                editor.putString("weather",responseText);
                                editor.apply();
                                mWeatherId=weather.basic.weatherID;
                                showWeatherInfo(weather);
                            }else {
                                Toast.makeText(WeatherActivity.this,"奥特曼可能不在家",Toast.LENGTH_SHORT).show();
                            }
                            swipeRefresh.setRefreshing(false);
                        }
                    });
            }
        });
    }

    private void loadBingPic() {
        final String requestBingpic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingpic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
    private void showWeatherInfo(Weather weather){
        String locationName=weather.basic.cityName;
        String updateTime = weather.update.updateTime;
        String weatherDegree=weather.now.tmperature+"℃";
        String weatherInfo=weather.now.cond_txt;
        titleLocation.setText(locationName);
        titleUpdatTime.setText(updateTime);
        weatherDegreeText.setText(weatherDegree);
        weatherInfoText.setText(weatherInfo);
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView condText = view.findViewById(R.id.cond_text);
            TextView tmpMax = view.findViewById(R.id.tmp_max_text);
            TextView tmpMin = view.findViewById(R.id.tmp_min_text);
            dateText.setText(forecast.date);
            condText.setText(forecast.cond_txt_d);
            tmpMax.setText(forecast.tmp_max);
            tmpMin.setText(forecast.tmp_min);
            forecastLayout.addView(view);
        }
        for (Lifestyle lifestyle : weather.lifestyleList) {
            typeText.setText(lifestyle.type);
            brfText.setText(lifestyle.brf);
            txtText.setText(lifestyle.txt);
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
