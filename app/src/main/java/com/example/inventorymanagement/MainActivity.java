package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CardView c1,c2,c3,c4,c5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c1 = findViewById(R.id.card1);
        c2 = findViewById(R.id.card2);
        c3 = findViewById(R.id.card3);
        c4 = findViewById(R.id.card4);
        c5 = findViewById(R.id.card5);

        setOnCardClicked(c1, 1);
        setOnCardClicked(c2, 2);
        setOnCardClicked(c3, 3);
        setOnCardClicked(c4, 4);
        setOnCardClicked(c5, 5);
    }

    private void setOnCardClicked(CardView card, int opt){
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (opt){
                    case 1:
                        startActivity(new Intent(MainActivity.this, UserManagement.class));
                        finish();
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, Categories.class));
                        finish();
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, Products.class));
                        finish();
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, Sales.class));
                        finish();
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, SalesReport.class));
                        finish();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}