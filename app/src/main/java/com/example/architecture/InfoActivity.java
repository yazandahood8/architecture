package com.example.architecture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    private static final String PREF_NAME = "PurchasePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);

        // Check if SharedPreferences exist


        // Set up GestureDetector for swipe detection
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Detect left-to-right swipe
                if (e1.getX() > e2.getX()) {
                    navigateToPlanPriceActivity();
                    return true;
                } else {
                    Toast.makeText(InfoActivity.this, "Swipe right-to-left to continue", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private void navigateToPlanPriceActivity() {

        if (hasSavedPreferences()) {
            navigateToLoginActivity();
            return;
        }

        Intent intent = new Intent(InfoActivity.this, PlanPriceActivity.class);
        startActivity(intent);
        finish(); // Optional: Close this activity
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: Close this activity
    }

    private boolean hasSavedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.contains("PurchaseDate"); // Replace with your key to check if data exists
    }
}
