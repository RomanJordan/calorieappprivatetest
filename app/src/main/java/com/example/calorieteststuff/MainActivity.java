package com.example.calorieteststuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddItemFragment.DialogListener {

    private FusedLocationProviderClient fusedLocationClient;

    ArrayList<Food> foodList = new ArrayList<>();
    JSONObject data = null;
    private RecyclerView mRecyclerView;
    private FoodAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AddItemFragment addItemFragment;

    public static TextView totalCaloriesText;
    private TextView weatherText;
    private TextView nameText;

    public static double totalCalories = 0;
    public static double caloricSurplusGoal = 0;
    public static double caloricDeficitGoal = 0;
    Double latitude, longitude;
    private final String key = "API";

    public static double getTotalCalories() {
        return totalCalories;
    }

    public static void setTotalCalories(double totalCalories) {
        MainActivity.totalCalories = totalCalories;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        totalCaloriesText = findViewById(R.id.totalCaloriesText);
        weatherText = findViewById(R.id.currentWeatherText);
        nameText = findViewById(R.id.nameText);
//        nameText.setText();
//        totalCaloriesText.setText("Total Calories:");
//        ArrayList<Food> exampleListOfFood = new ArrayList<>();
//        exampleListOfFood.add(new Food("Food 1", 150.0, R.drawable.ic_baseline_fastfood_24));

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FoodAdapter(this, foodList);
////        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new FoodAdapter(exampleListOfFood);
//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this,
                            Locale.getDefault());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("latitude", "" + latitude);
                    Log.d("longitude", "" + longitude);


                    getTempt(latitude, longitude);
                }
            }
        });
    }

    private void getTempt(Double x, Double y) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/onecall?lat="+ x +"&lon="+y+"&units=imperial&appid="+key);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while((tmp = br.readLine()) != null)
                json.append(tmp).append("\n");
            br.close();
            data = new JSONObject(json.toString());
            JSONObject currentArray = data.getJSONObject("current");
            String s = currentArray.getString("temp");
            System.out.println("Current Temperature is: "+s);
            weatherText.setText("Current Weather: "+s);
//            JSONArray js = data.getJSONArray("current");
//            System.out.println(js);
            System.out.println("All the cool data: "+data);
            if(data.getInt("cod") != 200) {
                System.out.println("Cancelled");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void addItem(View view) {
        Log.d("FloatingActionButton", "clicked");
        DialogFragment addFoodItemFragment = new AddItemFragment();
        addFoodItemFragment.show(getSupportFragmentManager(), "Add Food Item");


//        view.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void applyTexts(String foodName, String calories) {
        Log.d("FoodNameFromDialog",""+foodName);
        Log.d("CaloriesFromDialog",""+calories);
        totalCalories += Double.parseDouble(calories);
        if (AddItemFragment.isFood) {
            foodList.add(new Food(foodName, Double.parseDouble(calories), R.drawable.ic_baseline_fastfood_24));
        }
        else {
            foodList.add(new Food(foodName, Double.parseDouble(calories), R.drawable.ic_baseline_sports_tennis_24));
        }
        mAdapter.notifyDataSetChanged();
        totalCaloriesText.setText(Double.toString(totalCalories));
    }
}
