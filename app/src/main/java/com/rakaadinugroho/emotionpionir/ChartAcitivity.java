package com.rakaadinugroho.emotionpionir;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartAcitivity extends AppCompatActivity {
    private RadarChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_acitivity);
        chart   = (RadarChart) findViewById(R.id.radarChart);

        List<RadarEntry> entriesmorning = new ArrayList<>();
        List<RadarEntry> entriesevening = new ArrayList<>();

        entriesmorning.add(new RadarEntry(0.923f, "Marah"));
        entriesmorning.add(new RadarEntry(0.01f, "Benci"));
        entriesmorning.add(new RadarEntry(0.32f, "Rindu"));
        entriesmorning.add(new RadarEntry(0.02f, "Cinta"));
        entriesmorning.add(new RadarEntry(0.38f, "Kesal"));
        entriesmorning.add(new RadarEntry(0.001f, "Takut"));


        entriesevening.add(new RadarEntry(0.20f, "Marah"));
        entriesevening.add(new RadarEntry(0.02f, "Benci"));
        entriesevening.add(new RadarEntry(0.86f, "Rindu"));
        entriesevening.add(new RadarEntry(0.99f, "Cinta"));
        entriesevening.add(new RadarEntry(0.02f, "Kesal"));
        entriesevening.add(new RadarEntry(0.4f, "Takut"));

        RadarDataSet datasetmorning = new RadarDataSet(entriesmorning, "Morning");
        RadarDataSet datasetevening = new RadarDataSet(entriesevening, "Evening");

        datasetmorning.setColor(Color.CYAN);
        datasetevening.setColor(Color.DKGRAY);


        datasetmorning.setDrawFilled(true);
        datasetevening.setDrawFilled(true);

        List<IRadarDataSet> dataSets = new ArrayList<>();
        dataSets.add(datasetmorning);
        dataSets.add(datasetevening);



        final List<String> labels = new ArrayList<>();
        labels.add("Marah");
        labels.add("Takut");
        labels.add("Senang");
        labels.add("Netral");
        labels.add("Sebel");
        labels.add("Risih");

        XAxis xAxis = chart.getXAxis();
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        xAxis.setTextSize(8f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value % labels.size());
            }
        });
        RadarData data   = new RadarData(dataSets);
        chart.setDescription(null);
        chart.setData(data);
        chart.invalidate();
    }
}
