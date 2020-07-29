package com.example.calorieteststuff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddItemFragment.DialogListener {

    ArrayList<Food> foodList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private FoodAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AddItemFragment addItemFragment;

    public static TextView totalCaloriesText;

    public static double totalCalories = 0;

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
        totalCaloriesText = findViewById(R.id.totalCaloriesText);
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