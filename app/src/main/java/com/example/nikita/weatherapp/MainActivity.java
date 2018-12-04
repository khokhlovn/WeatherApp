package com.example.nikita.weatherapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, LocationListener {

    private static final String TAG = App.class.getName();
    RecyclerView recyclerView;
    List<com.example.nikita.weatherapp.List> posts;
    Fragment frag;
    FragmentTransaction fTrans;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posts = new ArrayList<>();

        recyclerView = findViewById(R.id.posts_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        CitiesAdapter adapter = new CitiesAdapter(posts);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        App.getApi().getData(Double.toString(getLastKnownLocation().getLatitude()),
                Double.toString(getLastKnownLocation().getLongitude()))
                .enqueue(new Callback<City>() {
                    @Override
                    public void onResponse(Call<City> call, Response<City> response) {
                        posts.addAll(response.body().getList());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<City> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", posts.get(position).getName());
        bundle.putString("country", posts.get(position).getSys().getCountry());
        bundle.putString("temperature", posts.get(position).getMain().getTempMax() - 273.15 + "C");
        bundle.putString("humidity", posts.get(position).getMain().getHumidity() + "%");
        bundle.putString("pressure", posts.get(position).getMain().getPressure() + " hPa");
        bundle.putString("wind", posts.get(position).getWind().getSpeed() + " meter/sec");
        frag = new CityFragment();
        frag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fTrans = fragmentManager.beginTransaction().replace(R.id.container, frag);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(TAG, "Широта=" + location.getLatitude());
            Log.d(TAG, "Долгота=" + location.getLongitude());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
        }
        lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
