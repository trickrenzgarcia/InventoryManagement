package com.example.inventorymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "inventory.db";
    public DBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE sales_report(id INTEGER PRIMARY KEY, date TEXT, description TEXT, sales DOUBLE, quantity INTEGER)");
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "category TEXT,"
                + "quantity INTEGER,"
                + "price DOUBLE"
                + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS sales_report");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public int getSalesSize(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM sales_report", null);
        c.moveToFirst();
        int rowSize = c.getInt(0);
        c.close();
        return rowSize;
    }

    public void insertSales(SalesReportModel salesReport){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", salesReport.getDate());
        values.put("description", salesReport.getDescription());
        values.put("sales", salesReport.getSales());
        values.put("quantity", salesReport.getQuantity());
        db.insert("sales_report", null, values);
        db.close();
    }


    public void insertProducts(ProductsModel products){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", products.getTitle());
        values.put("category", products.getCategory());
        values.put("quantity", products.getQuantity());
        values.put("price", products.getPrice());
        db.insert("products", null, values);
        db.close();
    }

    public ArrayList<String> getProductNames(){
        ArrayList<String> productNames = new ArrayList<>();
        String sql = "SELECT title FROM products";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                productNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return productNames;
    }

    public ArrayList<SalesReportModel> getAllSalesReport() {
        ArrayList<SalesReportModel> sales = new ArrayList<>();
        String strQuery = "SELECT * FROM sales_report";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);
        if(cursor.moveToFirst()){
            do {
                SalesReportModel salesModel = new SalesReportModel();
                salesModel.setDate(cursor.getString(1));
                salesModel.setDescription(cursor.getString(2));
                salesModel.setSales(cursor.getDouble(3));
                salesModel.setQuantity(cursor.getInt(4));
                sales.add(salesModel);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sales;
    }

    public ArrayList<ProductsModel> getAllProducts() {
        ArrayList<ProductsModel> products = new ArrayList<>();
        String strQuery = "SELECT * FROM products";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);
        if(cursor.moveToFirst()){
            do {
                ProductsModel productsModel = new ProductsModel();
                productsModel.setId(cursor.getInt(0));
                productsModel.setTitle(cursor.getString(1));
                productsModel.setCategory(cursor.getString(2));
                productsModel.setQuantity(cursor.getInt(3));
                productsModel.setPrice(cursor.getDouble(4));
                products.add(productsModel);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public void updateProductQuantity(int inputQuantity, int quantity, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        int newQuantity = quantity - inputQuantity;
        String query = "UPDATE products SET quantity = " + newQuantity + " WHERE title='"+ title + "'";
        db.execSQL(query);
    }

    public int getProductQuantitySize(String selectedString){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM products WHERE title='" + selectedString + "'", null);
        cursor.moveToFirst();
        int row = cursor.getInt(0);
        cursor.close();
        db.close();
        return row;
    }

    public double getProductPrice(String str){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT price FROM products WHERE title='" + str + "'", null);
        cursor.moveToFirst();
        double row = cursor.getDouble(0);
        cursor.close();
        db.close();
        return row;
    }


}
