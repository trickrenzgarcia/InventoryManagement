package com.example.inventorymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SalesItemAdapter extends RecyclerView.Adapter<SalesItemAdapter.SalesViewHolder> {

    private ArrayList<SalesReportModel> itemList;

    public SalesItemAdapter(ArrayList<SalesReportModel> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sales, parent, false);
        return new SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position){
        SalesReportModel item = itemList.get(position);
        holder.titleTextView.setText(item.getDate()); // a****************
        holder.descriptionTextView.setText(item.getDescription()); // *****************
        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        holder.salesTextView.setText(dollarFormat.format(item.getSales()));
    }

    @Override
    public int getItemCount(){
        return itemList.size();
    }

    public static class SalesViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView salesTextView;

        public SalesViewHolder(@NonNull View itemView){
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            salesTextView = itemView.findViewById(R.id.salesPrice);
        }
    }
}