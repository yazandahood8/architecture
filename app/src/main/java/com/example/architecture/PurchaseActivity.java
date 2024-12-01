package com.example.architecture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class PurchaseActivity extends AppCompatActivity {

    private RelativeLayout purchaseLayout;
    private EditText cardNumberInput, dateInput, cvvInput;
    private TextView tv1, tv2, tv3, sub;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "PurchasePrefs";
    private static final String PREF_DATE_KEY = "PurchaseDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        // Initialize views
        purchaseLayout = findViewById(R.id.purchaseLayout);
        cardNumberInput = findViewById(R.id.card_number);
        dateInput = findViewById(R.id.date);
        cvvInput = findViewById(R.id.cvv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        sub = findViewById(R.id.sub);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Get the value passed from PlanPriceActivity
        int value = getIntent().getIntExtra("value", 0);

        // Button listeners
        tv1.setOnClickListener(v -> validateInputs(value));
        tv2.setOnClickListener(v -> validateInputs(value));
        tv3.setOnClickListener(v -> validateInputs(value));

        sub.setVisibility(View.GONE);

        sub.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void validateInputs(int value) {
        String cardNumber = cardNumberInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String cvv = cvvInput.getText().toString().trim();

        // Validate card number (16 digits)
        if (TextUtils.isEmpty(cardNumber) || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            cardNumberInput.setError("Enter a valid 16-digit card number");
            return;
        }

        // Validate date (MM/YY format)
        if (TextUtils.isEmpty(date) || !date.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            dateInput.setError("Enter a valid date (MM/YY)");
            return;
        }

        // Validate CVV (3 digits)
        if (TextUtils.isEmpty(cvv) || cvv.length() != 3 || !cvv.matches("\\d+")) {
            cvvInput.setError("Enter a valid 3-digit CVV");
            return;
        }

        // Set the background and update the date
        if (value == 10) {
            purchaseLayout.setBackgroundResource(R.drawable.receipt10);
            updateDate(1, Calendar.MONTH);
        } else if (value == 50) {
            purchaseLayout.setBackgroundResource(R.drawable.receipt50);
            updateDate(1, Calendar.YEAR);
        }

        // Show success message
        Toast.makeText(this, "Inputs are valid!", Toast.LENGTH_SHORT).show();

        // Hide inputs and show submission button
        cardNumberInput.setVisibility(View.GONE);
        dateInput.setVisibility(View.GONE);
        cvvInput.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
        sub.setVisibility(View.VISIBLE);
    }

    private void updateDate(int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, amount);

        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int year = calendar.get(Calendar.YEAR) % 100;

        String formattedDate = String.format("%02d/%02d", month, year);
        dateInput.setText(formattedDate);

        // Save the date to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_DATE_KEY, formattedDate);
        editor.apply();
    }
}
