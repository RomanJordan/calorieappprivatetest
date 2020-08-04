package com.example.calorieteststuff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstLaunch extends AppCompatActivity {

    private EditText nameInput;
    private EditText ageInput;
    private Button saveButton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_launch);

        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        if (!nameInput.getText().toString().equals("") && !ageInput.getText().toString().equals("")) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, nameInput.getText().toString());
            editor.apply();
            Toast.makeText(this, "Name Saved: "+sharedPreferences.getString(TEXT, ""), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(FirstLaunch.this, MainActivity.class));
        }
        if (nameInput.getText().toString().equals("")) {
            Toast.makeText(this, "Name field is Empty!", Toast.LENGTH_SHORT).show();

        }
        if (ageInput.getText().toString().equals("")) {
            Toast.makeText(this, "Age field is Empty!", Toast.LENGTH_SHORT).show();
        }

    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

}
