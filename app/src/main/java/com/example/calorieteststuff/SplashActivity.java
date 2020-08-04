package com.example.calorieteststuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        reference();
        runSplash();
    }


    private void reference(){
        TextView text = findViewById(R.id.text);
        ImageView image = findViewById(R.id.image);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        text.startAnimation(myanim);
        image.startAnimation(myanim);

    }
    private void runSplash(){
        final Intent main = new Intent(this, MainActivity.class);

        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    Boolean isFirstRun = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                            .getBoolean("isFirstRun", true);
                    if (isFirstRun) {
                        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit()
                                .putBoolean("isFirstRun", false).apply();
                        startActivity(new Intent(SplashActivity.this, FirstLaunch.class));
                        Toast.makeText(SplashActivity.this, "First Run", Toast.LENGTH_LONG)
                                .show();

                    }
                    else {
                        startActivity(main);
                        finish();
                    }
                    getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit()
                            .putBoolean("isFirstRun", false).apply();

                }
            }

        };
        timer.start();

    }
}
