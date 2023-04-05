package com.example.inventorymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DataChart extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button downloadPdfChart;
    private DBHelper dbHelper;
    private ArrayList<SalesReportModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_chart);

        ImageView backBtn = findViewById(R.id.back_button);
        downloadPdfChart = findViewById(R.id.downloadPdfChartButton);
        BarChart barChart = findViewById(R.id.barChart);
        dbHelper = new DBHelper(this);
        itemList = new ArrayList<>();
        itemList = dbHelper.getAllSalesReport();

        ArrayList<BarEntry> visitors = new ArrayList<>();

        int index = 0;
        for (SalesReportModel sale: itemList) {
            visitors.add(new BarEntry(index, (float) sale.getSales()));
            index++;
        }

        BarDataSet barDataSet = new BarDataSet(visitors, "Monthly Sales");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Sales Report Bar Chart");
        barChart.animateY(2000);

        downloadPdfChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(DataChart.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DataChart.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    generateChartPDF();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataChart.this, SalesReport.class));
            }
        });
    }

    public void generateChartPDF(){
        PdfDocument document = new PdfDocument();

        try {
            // Get a reference to your chart view
            BarChart chartView = findViewById(R.id.barChart);

            // Render the chart view to a bitmap
            Bitmap chartBitmap = Bitmap.createBitmap(chartView.getWidth(), chartView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas chartCanvas = new Canvas(chartBitmap);
            chartView.draw(chartCanvas);

            PdfDocument.PageInfo chartPageInfo = new PdfDocument.PageInfo.Builder(500, 600, 1).create();
            PdfDocument.Page chartPage = document.startPage(chartPageInfo);

            float scaleFactor = Math.min((float) chartPageInfo.getPageWidth() / chartBitmap.getWidth(), (float) chartPageInfo.getPageHeight() / chartBitmap.getHeight());
            Bitmap scaledChartBitmap = Bitmap.createScaledBitmap(chartBitmap, (int) (chartBitmap.getWidth() * scaleFactor), (int) (chartBitmap.getHeight() * scaleFactor), true);

            Canvas chartPdfCanvas = chartPage.getCanvas();
            chartPdfCanvas.drawBitmap(scaledChartBitmap, null, new Rect(0, 0, chartPageInfo.getPageWidth(), chartPageInfo.getPageHeight()), null);

            // Finish the active page containing the chart before closing the document
            document.finishPage(chartPage);

            // Save the PDF document
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sales_report_chart.pdf");
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            document.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
            document.close();

            Toast.makeText(DataChart.this, "PDF generated and downloaded.", Toast.LENGTH_SHORT).show();

            // Create the content URI for the file
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", outputFile);

            // Grant temporary read permission to the content URI
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start the PDF viewer activity
            startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateChartPDF();
            } else {
                Toast.makeText(DataChart.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(DataChart.this, SalesReport.class);
        startActivity(intent);
        finish();
    }
}