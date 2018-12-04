package com.example.nikita.weatherapp;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static WeatherApiInterface weatherApi;
    private Retrofit retrofit;

    public static WeatherApiInterface getApi() {
        return weatherApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApi = retrofit.create(WeatherApiInterface.class);
    }
}
