package com.example.architecture;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);

        // Set up GestureDetector for swipe detection
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Detect left-to-right swipe
                if (e1.getX() > e2.getX()) {
                    navigateToAnotherActivity();
                    return true;
                }
                else{
                    Toast.makeText(InfoActivity.this, "Swap right->left to continue", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private void navigateToAnotherActivity() {
        Intent intent = new Intent(InfoActivity.this, LoginActivity.class); // Replace 'AnotherActivity' with the target activity
        startActivity(intent);
        finish(); // Optional: Close this activity
    }
}
