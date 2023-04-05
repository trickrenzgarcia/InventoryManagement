package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Products extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayList<ProductsModel> itemProducts;
    private ImageView backBtn;
    Button btnAdd;
    TextInputEditText editTitle, editQuantity, editPrice;
    RecyclerView recyclerView;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        dbHelper = new DBHelper(this);
        backBtn = findViewById(R.id.back_button_products);
        itemProducts = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewProduct);
        btnAdd = findViewById(R.id.btnAdd);
        editQuantity = findViewById(R.id.editQuantity);
        editTitle = findViewById(R.id.editTitle);
        editPrice = findViewById(R.id.editPrice);
        autoCompleteTextView = findViewById(R.id.filled_exposed);

        ProductRecycleAdapter();

        CategorySpinner();
        AddProduct();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Products.this, MainActivity.class));
            }
        });

    }

    public void ProductRecycleAdapter(){
        itemProducts = dbHelper.getAllProducts();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ProductsAdapter adapter = new ProductsAdapter(itemProducts);
        recyclerView.setAdapter(adapter);
    }

    public void AddProduct(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTitle.getText().equals("") || editQuantity.getText().equals("") || editQuantity.getText().equals("0") || autoCompleteTextView.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Don't leave empty fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if(editTitle.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "Please Enter a product name!", Toast.LENGTH_SHORT).show();
                    else if (editQuantity.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "Please Enter a Quantity of a product!", Toast.LENGTH_SHORT).show();
                    else if(autoCompleteTextView.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "Please Select a category!", Toast.LENGTH_SHORT).show();
                    else if (editPrice.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "Please Enter a Price of a product!", Toast.LENGTH_SHORT).show();
                    else if (Integer.parseInt(editQuantity.getText().toString()) <= 0)
                        Toast.makeText(getApplicationContext(), "Quantity must be greater than 0!", Toast.LENGTH_SHORT).show();
                    else if (Integer.parseInt(editPrice.getText().toString()) <= 0)
                        Toast.makeText(getApplicationContext(), "Price must be greater than 0!", Toast.LENGTH_SHORT).show();
                    else {
                        String title = editTitle.getText().toString();
                        String category = autoCompleteTextView.getText().toString();
                        int quantity = Integer.parseInt(editQuantity.getText().toString());
                        double price = Double.parseDouble(editPrice.getText().toString());
                        dbHelper.insertProducts(new ProductsModel(title, category, quantity, price));
                        Toast.makeText(getApplicationContext(), "Product is added to the database!", Toast.LENGTH_SHORT).show();
                        editQuantity.setText("");
                        editTitle.setText("");
                        ProductRecycleAdapter();
                    }
                }
            }
        });
    }

    public void CategorySpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_categories, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Products.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}