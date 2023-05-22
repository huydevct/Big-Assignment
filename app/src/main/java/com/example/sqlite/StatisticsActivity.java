package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sqlite.dal.SQLiteHelper;
import com.example.sqlite.fragment.FragmentHome;
import com.example.sqlite.model.Item;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private BarChart barChart;
    private Button btnBackStatis;
    private SQLiteHelper db;
    private List<Item> list;
    private ArrayList<BarEntry> entries;
    private final String[] categories = {"Mua sắm", "Ăn uống", "Tiền nhà", "Điện nước", "Đi lại", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statisics);
        initData();
        Intent intent = getIntent();
        String month = intent.getStringExtra("month");
        showChart(month);

        btnBackStatis.setOnClickListener(v -> {
            Intent intent1 = new Intent(StatisticsActivity.this, MainActivity.class);
            startActivity(intent1);
        });
    }
    private void showChart(String month) {
        db = new SQLiteHelper(this);

        if (month.equals("Tất cả")) {
            list = db.getAll();
        } else {
            Timestamp start = getStartTimestamp(Integer.parseInt(month)-1);
            Timestamp end = getEndTimestamp(Integer.parseInt(month)-1);
            String S = new Date(start.getTime()).toString().substring(8,10);
            String E = new Date(end.getTime()).toString().substring(8,10);
            list = db.searchByDate(S, month , E, month);
        }

        if (list.size() == 0) {
            for (int i = 0; i < categories.length; i++) {
                entries.add(new BarEntry(Float.parseFloat(String.valueOf(i)), 0));
            }
        } else {
            for (int i = 0; i < categories.length; i++) {
                List<Item> tempList = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    Item item = list.get(j);
                    if (item.getCategory().equals(categories[i])) {
                        tempList.add(list.get(j));
                    }
                }
                int sum = tempList.size() > 0 ? sumPrice(tempList) : 0;
                entries.add(new BarEntry(Float.parseFloat(String.valueOf(i)), sum));
            }
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(Color.LTGRAY, Color.GREEN, Color.YELLOW, Color.WHITE, Color.MAGENTA, Color.CYAN, Color.DKGRAY);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(18f);
        BarData barData = new BarData(barDataSet);
        barData.notifyDataChanged();

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.setBackgroundColor(Color.DKGRAY);
        barChart.getDescription().setText("");
    }

    private int sumPrice(List<Item> list) {
        int sum = 0;
        for (Item i : list) {
            sum += Integer.parseInt(i.getPrice());
        }
        return sum;
    }

    private void initData() {
        entries = new ArrayList<>();
        barChart = findViewById(R.id.barChart);
        btnBackStatis = findViewById(R.id.btnBackStatis);
        initBarChart();
    }

    private void initBarChart() {
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawBorders(false);
        barChart.animateY(1000);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextSize(14f);
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setTextSize(14f);
        rightAxis.setTextColor(Color.WHITE);

        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }

    public static Timestamp getStartTimestamp(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getEndTimestamp(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
}