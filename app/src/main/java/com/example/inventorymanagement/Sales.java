package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Sales extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Sales.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}