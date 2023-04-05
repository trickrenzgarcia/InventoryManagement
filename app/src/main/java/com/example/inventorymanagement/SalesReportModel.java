package com.example.inventorymanagement;

public class SalesReportModel {
    private String date;
    private String description;
    private double sales;
    private int quantity;

    public SalesReportModel(){}

    public SalesReportModel(String date, String description, double sales, int quantity){
        this.date = date;
        this.description = description;
        this.sales = sales;
        this.quantity = quantity;
    }

    public String getDate(){
        return date;
    }

    public String getDescription(){
        return description;
    }

    public double getSales(){
        return sales;
    }

    public int getQuantity() { return quantity; }

    public void setDate(String date){
        this.date = date;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setSales(double sales){
        this.sales = sales;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }


}
