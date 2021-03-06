package com.example.calorieteststuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
import java.util.Calendar;
import java.util.Locale;
import static com.example.calorieteststuff.Notification.CHANNEL_1_ID;
import android.app.Notification;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AddItemFragment.DialogListener{

    private FusedLocationProviderClient fusedLocationClient;
    private NotificationManagerCompat notificationManager;

    ArrayList<Food> foodList = new ArrayList<>();
    JSONObject data = null;
    private RecyclerView mRecyclerView;
    private FoodAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AddItemFragment addItemFragment;

    public static TextView totalCaloriesText;
    private TextView weatherText;
    private TextView nameText;
    private TextView weightText;
    private TextView heightText;

    public static double totalCalories = 0;
    public static double caloricSurplusGoal = 0;
    public static double caloricDeficitGoal = 0;
    Double latitude, longitude;
    private final String key = "77bf636fc7895dfb8cbf7acd259b2016";

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
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        totalCaloriesText = findViewById(R.id.totalCaloriesText);
        weatherText = findViewById(R.id.currentWeatherText);
        nameText = findViewById(R.id.nameText);
        weightText = findViewById(R.id.weightText);
        heightText = findViewById(R.id.heightText);
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
        notificationManager = NotificationManagerCompat.from(this);
        sendOnChannel1();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 8);
        c.set(Calendar.MINUTE, 0);
        startAlarm(c);
        String nameKeyText = prefs.getString("nameKey", "Name Not Set");
        String weightKeyText = prefs.getString("weightKey", "Weight Not Set");
        String heightKeyText = prefs.getString("heightKey", "Height Not Set");
        nameText.setText(nameKeyText);
        weightText.setText(weightKeyText);
        heightText.setText(heightKeyText);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, i, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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

//    public void showPopup(View v) {
//        PopupMenu popup = new PopupMenu(this, v);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.actions, popup.getMenu());
//        popup.show();
//    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.shareButton:
//                Toast.makeText(getApplicationContext(),"Brag to your friends",Toast.LENGTH_LONG).show();
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SENDTO);
//                shareIntent.setData(Uri.parse("smsto:"+totalCalories));
//                startActivity(shareIntent);
//                return true;
//            case R.id.logoutButton:
//                FirebaseAuth.getInstance().signOut();
//                Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
//                startActivity(i);
//                return true;
//            default:
//                return false;
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsButton:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
            case R.id.shareButton:
                Toast.makeText(getApplicationContext(),"Brag to your friends",Toast.LENGTH_LONG).show();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SENDTO);
                shareIntent.setData(Uri.parse("smsto:"));
                shareIntent.putExtra("sms_body", "Total Calories for today: "+totalCalories);
                startActivity(shareIntent);
                return true;
            case R.id.logoutButton:
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(logout);
                return true;

        }
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

    public void sendOnChannel1() {
        String title = "MyCoolApp";
        String message = "Welcome to my cool app";
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_settings_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

    public void showPopup(MenuItem item) {
    }
}
