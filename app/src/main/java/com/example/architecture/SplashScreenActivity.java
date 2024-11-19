package com.example.architecture;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Delay to show the splash screen for a few seconds
        new Handler().postDelayed(() -> {
            // After the delay, navigate to the Main Activity
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close Splash Screen Activity
        }, SPLASH_DURATION);

        ImageView gifBackground = findViewById(R.id.backgroundGif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.giphy) // Replace with your GIF drawable
                .into(gifBackground);
    }
}
