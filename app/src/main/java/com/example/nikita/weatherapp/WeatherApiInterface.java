package com.example.nikita.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiInterface {
    @GET("find?&cnt=20&appid=93355bdd23d1c5bc7d03e74c516ab118")
    Call<City> getData(@Query("lat") String latitude, @Query("lon") String longitude);
}
