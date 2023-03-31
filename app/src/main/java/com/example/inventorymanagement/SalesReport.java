package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SalesReport extends AppCompatActivity {

    private ImageView backbtn;
    private CardView monthlyBtn, dailyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        backbtn = (ImageView) findViewById(R.id.back_button);
        monthlyBtn = (CardView) findViewById(R.id.monthly_sales_card);
        dailyBtn = (CardView) findViewById(R.id.daily_sales_card);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalesReport.this, MainActivity.class));
                finish();
            }
        });

        monthlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dailyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(SalesReport.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}