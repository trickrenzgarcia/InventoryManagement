package com.example.inventorymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class SalesReport extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ImageView backbtn;
    private TextView txtHistory;
    private CardView monthlyBtn, chartBtn;
    private DBHelper dbHelper;
    private ArrayList<SalesReportModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        backbtn = (ImageView) findViewById(R.id.back_button);
        monthlyBtn = (CardView) findViewById(R.id.monthly_sales_card);
        chartBtn = (CardView) findViewById(R.id.data_chart_card);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button downloadPDF = (Button) findViewById(R.id.downloadButton);
        dbHelper = new DBHelper(this);
        itemList = new ArrayList<>();
        downloadPDF.setVisibility(View.GONE);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalesReport.this, MainActivity.class));
                finish();
            }
        });

        monthlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemList.clear();
                downloadPDF.setVisibility(View.VISIBLE);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                if(dbHelper.getSalesSize() <= 0){
                    dbHelper.insertSales(new SalesReportModel("1/2/2023", "sales from jan to feb", 12345, 136));
                    dbHelper.insertSales(new SalesReportModel("2/2/2023", "sales from feb to mar", 15324, 130));
                    dbHelper.insertSales(new SalesReportModel("3/2/2023", "sales from mar to april", 17345, 189));
                    dbHelper.insertSales(new SalesReportModel("4/2/2023", "sales from april to may", 16052, 175));
                    dbHelper.insertSales(new SalesReportModel("6/2/2023", "sales from may to june", 11632, 130));
                    dbHelper.insertSales(new SalesReportModel("7/2/2023", "sales from june to july", 17345, 189));
                }
                itemList = dbHelper.getAllSalesReport();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                SalesAdapter adapter = new SalesAdapter(itemList);
                recyclerView.setAdapter(adapter);

            }
        });

        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalesReport.this, DataChart.class));
                finish();
            }
        });

        downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SalesReport.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SalesReport.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    generatePDF();
                }
            }
        });
    }

    private void generatePDF() {
        try {
            Document document = new Document();
            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "monthly_sales_report.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            // Title
            Paragraph title = new Paragraph("BroJava Inventory Manage");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add date
            Paragraph date = new Paragraph(new Date().toString());
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);

            PdfPTable table = new PdfPTable(4);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Sales Price");
            table.addCell("Sales Quantity");

            for(SalesReportModel sale: itemList){
                table.addCell(sale.getDate());
                table.addCell(sale.getDescription());
                table.addCell(String.valueOf(sale.getSales()));
                table.addCell(String.valueOf(sale.getQuantity()));
            }
            document.add(table);
            document.close();

            Toast.makeText(SalesReport.this, "PDF generated and downloaded.", Toast.LENGTH_SHORT).show();

            // Create the content URI for the file
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", pdfFile);

            // Grant temporary read permission to the content URI
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start the PDF viewer activity
            startActivity(intent);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF();
            } else {
                Toast.makeText(SalesReport.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(SalesReport.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}