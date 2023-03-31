package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class UserManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(UserManagement.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}