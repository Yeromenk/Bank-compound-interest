package com.example.cv4;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BarChart chart;
    private List<BarEntry> vkladEntries;
    private List<BarEntry> urokyEntries;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private TextView textView12;
    private TextView textView13;
    private TextView textView9;
    private TextView textView10;
    private TextView textView11;

    private SharedPreferences.Editor editor;




    private void setupBarChart() {
        BarDataSet vkladDataSet = new BarDataSet(vkladEntries, "Vklad");
        vkladDataSet.setColor(Color.BLUE);

        BarDataSet urokyDataSet = new BarDataSet(urokyEntries, "Uroky");
        urokyDataSet.setColor(Color.GREEN);

        BarData barData = new BarData(vkladDataSet, urokyDataSet);

        chart.setData(barData);
        chart.invalidate();
    }


    public void calculateInvestment() {
        int depositAmount = seekBar1.getProgress();
        float interestRate = (float) seekBar2.getProgress() / 100.0f;
        int investmentYears = seekBar3.getProgress();

        double result = depositAmount * Math.pow(1 + interestRate, investmentYears);
        double profit = result - depositAmount;

        textView12.setText(String.format("%.2f", result));
        textView13.setText(String.format("%.2f", profit));

        vkladEntries.set(0, new BarEntry(1, depositAmount));
        urokyEntries.set(0, new BarEntry(2F, (float) profit));
        setupBarChart();

        editor.putFloat("nasporena suma", (float) result);
        editor.putFloat("z toho uroky", (float) profit);
        editor.putInt("vklad", depositAmount);
        editor.putInt("urok", (int) interestRate);
        editor.putInt("doba", investmentYears);
        editor.apply();
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        chart = findViewById(R.id.chart);
        vkladEntries = new ArrayList<>();
        urokyEntries = new ArrayList<>();

        vkladEntries.add(new BarEntry(1, 10000));
        urokyEntries.add(new BarEntry(2, 100));

        seekBar1 = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);
        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        textView13 = findViewById(R.id.textView_13);

        int vkladValue = sharedPreferences.getInt("vklad", 10000);
        int urokValue = sharedPreferences.getInt("urok", 1);
        int dobaValue = sharedPreferences.getInt("doba", 1);
        float nasporenaSuma = sharedPreferences.getFloat("nasporena suma", 10000.0f);
        float zTohoUroky = sharedPreferences.getFloat("z toho uroky", 1000.0f);

        textView9.setText(String.valueOf(vkladValue));
        textView10.setText(String.valueOf(urokValue));
        textView11.setText(String.valueOf(dobaValue));
        textView12.setText(String.valueOf(nasporenaSuma));
        textView13.setText(String.valueOf(zTohoUroky));

        Button saveButton = findViewById(R.id.button);

        setupBarChart();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String record = "Vklad: " + textView9.getText().toString() + "\n" + "Urok: " + textView10.getText().toString() + "\n" + "Obdobi: " + textView11.getText().toString() + "\n" + "Nasporena suma: " + textView12.getText().toString() + "\n" + "Z toho uroky: " + textView13.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferences", MODE_PRIVATE);
                Set<String> historicalDataSet = sharedPreferences.getStringSet("history", new HashSet<String>());


                historicalDataSet.add(record);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("history", historicalDataSet);
                editor.apply();
            }
        });

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView9.setText(String.valueOf(progress));
                int step = 10000;
                int newValue = Math.round(progress / step) * step;
                seekBar.setProgress(newValue);
                calculateInvestment();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView10.setText(String.valueOf(progress));
                int step = 1;
                int newValue = Math.round(progress / step) * step;
                seekBar.setProgress(newValue);
                calculateInvestment();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView11.setText(String.valueOf(progress));
                int step = 1;
                int newValue = Math.round(progress / step) * step;
                seekBar.setProgress(newValue);
                calculateInvestment();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu1) {
            Intent intent = new Intent(MainActivity.this, CharTypeActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}