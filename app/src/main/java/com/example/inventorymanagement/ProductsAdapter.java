package com.example.inventorymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private ArrayList<ProductsModel> itemList;

    public ProductsAdapter(ArrayList<ProductsModel> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_products, parent, false);
        return new ProductsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.MyViewHolder holder, int position){
        ProductsModel item = itemList.get(position);
        holder.idView.setText(Integer.toString(item.getId()));
        holder.titleView.setText(item.getTitle());
        holder.categoryView.setText("Category: " + item.getCategory());
        holder.quantityView.setText("Quantity: " + Integer.toString(item.getQuantity()));
        holder.priceView.setText("Price: $" + Double.toString(item.getPrice()));
    }

    @Override
    public int getItemCount(){
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idView;
        TextView titleView;
        TextView categoryView;
        TextView quantityView;
        TextView priceView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            idView = itemView.findViewById(R.id.pid);
            titleView = itemView.findViewById(R.id.ptitle);
            categoryView = itemView.findViewById(R.id.pcategory);
            quantityView = itemView.findViewById(R.id.pquantity);
            priceView = itemView.findViewById(R.id.pprice);
        }
    }
}