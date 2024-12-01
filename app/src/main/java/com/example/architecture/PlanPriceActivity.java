package com.example.architecture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PlanPriceActivity extends AppCompatActivity {
    private TextView tvTen, tvFifty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_price);

        tvTen = findViewById(R.id.tvTen);
        tvFifty = findViewById(R.id.tvFifty);

        tvTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlanPriceActivity.this, "tvTen Clicked!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PlanPriceActivity.this,PurchaseActivity.class);
                intent.putExtra("value",10);
                startActivity(intent);
            }
        });

        tvFifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlanPriceActivity.this, "tvFifty Clicked!", Toast.LENGTH_SHORT).show();
                // Change the background image
                Intent intent=new Intent(PlanPriceActivity.this,PurchaseActivity.class);
                intent.putExtra("value",50);
                startActivity(intent);
            }
        });
    }
}
