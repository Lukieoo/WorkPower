package com.anioncode.workpower.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anioncode.workpower.R;
import com.db.williamchart.view.BarChartView;
import com.db.williamchart.view.LineChartView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphFragment extends Fragment {
    LineChart chartView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_layout, container, false);

        chartView = view.findViewById(R.id.lineChartx);


        LineData data = getData(15, 100);

        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(chartView, data, Color.TRANSPARENT);
        return view;
    }


    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);


        chart.getAxisRight().setEnabled(false);

        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(10, false);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();

        rightAxis.setLabelCount(10, false);
        rightAxis.setDrawGridLines(false);

        rightAxis.setDrawAxisLine(false); // this replaces setStartAtZero(true)
        chart.setData(data);
        // set data



        chart.invalidate();
        chart.animateX(2750);
    }

    private LineData getData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);


        // create a data object with the data sets
        return new LineData(set1);
    }

    //New Section

}
