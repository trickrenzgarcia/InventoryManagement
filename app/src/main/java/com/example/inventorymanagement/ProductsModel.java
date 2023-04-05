package com.example.inventorymanagement;

public class ProductsModel {
    private int id;
    private String title;
    private String category;
    private int quantity;
    private double price;

    public ProductsModel() {}

    public ProductsModel(String title, String category, int quantity, double price){
        this.title = title;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setPrice(double price) { this.price = price; }

    public double getPrice() { return price; }

}
