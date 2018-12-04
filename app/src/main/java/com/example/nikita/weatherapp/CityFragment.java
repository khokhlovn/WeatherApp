package com.example.nikita.weatherapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city,
                container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView tvName = getView().findViewById(R.id.tv_name);
        TextView tvCountry = getView().findViewById(R.id.tv_country);
        TextView tvTemperature = getView().findViewById(R.id.tv_temperature);
        TextView tvHumidity = getView().findViewById(R.id.tv_humidity);
        TextView tvPressure = getView().findViewById(R.id.tv_pressure);
        TextView tvWind = getView().findViewById(R.id.tv_wind);
        tvName.setText("City: " + getArguments().getString("name"));
        tvCountry.setText("Country: " + getArguments().getString("country"));
        tvTemperature.setText("Temperature: " + getArguments().getString("temperature"));
        tvHumidity.setText("Humidity: " + getArguments().getString("humidity"));
        tvPressure.setText("Pressure: " + getArguments().getString("pressure"));
        tvWind.setText("Wind: " + getArguments().getString("wind"));
    }
}
