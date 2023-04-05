package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class Sales extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayList<SalesReportModel> itemSales;
    private ImageView backBtn;
    private MaterialButton btnAdd;
    private TextInputLayout etPrice_layout, etQuantity_layout;
    private TextInputEditText etQuantity, etPrice, etDescription, tvDate;
    private RecyclerView recyclerView;
    private DatePickerDialog.OnDateSetListener setListener;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        etQuantity = findViewById(R.id.editQuantitySales); // TextInputEditText for quantity
        etPrice = findViewById(R.id.editPriceSales); // TextInputEditText for price
        etDescription = findViewById(R.id.editDescriptionSales); // TextInputEditText for description
        tvDate = findViewById(R.id.tv_date); // textView(TextInputEditText) for date picker
        btnAdd = findViewById(R.id.btnAdd); // Add Button for inserting
        recyclerView = findViewById(R.id.recyclerViewSales); // RecyclerView
        dbHelper = new DBHelper(this); // SQLite Database
        itemSales = new ArrayList<>(); // ArrayList with SalesReportModel type
        backBtn = findViewById(R.id.back_button_sales); // Back Button
        autoCompleteTextView = findViewById(R.id.filled_exposed); // Product Spinner
        etPrice_layout = findViewById(R.id.etPrice_layout);
        etQuantity_layout = findViewById(R.id.etQuantity_layout);

        onDateClicked();
        SalesRecycleAdapter();
        ProductsSpinner();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autoCompleteTextView.getText().toString().equals(""))
                    Toast.makeText(Sales.this, "Select a product!", Toast.LENGTH_SHORT).show();
                else if(etQuantity.getText().toString().equals("") || etQuantity.getText().toString().equals("0"))
                    Toast.makeText(Sales.this, "Invalid Quantity!", Toast.LENGTH_SHORT).show();
                else if(etPrice.getText().toString().equals("") || etPrice.getText().toString().equals("0"))
                    Toast.makeText(Sales.this, "Invalid Price!", Toast.LENGTH_SHORT).show();
                else if(etDescription.getText().toString().equals(""))
                    Toast.makeText(Sales.this, "Empty Description!", Toast.LENGTH_SHORT).show();
                else if(tvDate.getText().toString().equals("Select Date") || tvDate.getText().toString().equals(""))
                    Toast.makeText(Sales.this, "Please Select a date!", Toast.LENGTH_SHORT).show();
                else {
                    String date = tvDate.getText().toString();
                    String description = etDescription.getText().toString();
                    double salesPrice = Double.parseDouble(etPrice.getText().toString());
                    int quantity = Integer.parseInt(etQuantity.getText().toString());
                    int dbQuantity = dbHelper.getProductQuantitySize(autoCompleteTextView.getText().toString());

                    dbHelper.updateProductQuantity(quantity, dbQuantity, autoCompleteTextView.getText().toString());
                    Toast.makeText(Sales.this, "Updated! " + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();

                    dbHelper.insertSales(new SalesReportModel(date, description, salesPrice, quantity));
                    Toast.makeText(Sales.this, "New Sales is successfully Added!", Toast.LENGTH_SHORT).show();
                    clearFields();
                    SalesRecycleAdapter();
                }

            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals("")){
                    int quantity = dbHelper.getProductQuantitySize(autoCompleteTextView.getText().toString());
                    etQuantity_layout.setHint("product quantity: "+String.valueOf(quantity));
                    etQuantity.setText("");
                    etPrice.setText("");
                    etDescription.setText("");
                    tvDate.setText("");
                } else {
                    etQuantity_layout.setHint("Select a product to show quantity!");
                    etQuantity.setText("");
                    etPrice.setText("");
                    etDescription.setText("");
                    tvDate.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etPrice_layout.setHint("Input quantity first!");
                etPrice.setText("sales = price * quantity");
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.toString().equals("")){
                    etPrice_layout.setHint("Input quantity first!");
                    etPrice.setText("");
                } else {
                    etPrice_layout.setHint("Price Sales");
                    int inputQuantity = Integer.parseInt(etQuantity.getText().toString());
                    if(!autoCompleteTextView.getText().toString().equals("")){
                        int dbQuantity = dbHelper.getProductQuantitySize(autoCompleteTextView.getText().toString());
                        double dbPrice = dbHelper.getProductPrice(autoCompleteTextView.getText().toString());
                        if(inputQuantity > dbQuantity){
                            etQuantity.setText("");
                            etQuantity_layout.setHint("product quantity: "+String.valueOf(dbQuantity));
                            Toast.makeText(Sales.this, "Max quantity: " + dbQuantity, Toast.LENGTH_SHORT).show();
                        } else {
                            double sales = dbPrice * inputQuantity;
                            etPrice.setText(String.valueOf(sales));
                        }
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    public void SalesRecycleAdapter(){
        itemSales = dbHelper.getAllSalesReport();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SalesItemAdapter adapter = new SalesItemAdapter(itemSales);
        recyclerView.setAdapter(adapter);
    }

    public void ProductsSpinner(){
        ArrayList<String> productNames = dbHelper.getProductNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drop_down_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                Toast.makeText(Sales.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onDateClicked(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Sales.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = day + "/"+month+"/"+year;
                tvDate.setText(date);
            }
        };
    }

    private void clearFields(){
        autoCompleteTextView.setText("");
        etQuantity.setText("");
        etPrice.setText("");
        etDescription.setText("");
        tvDate.setText("Select Date");
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Sales.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}