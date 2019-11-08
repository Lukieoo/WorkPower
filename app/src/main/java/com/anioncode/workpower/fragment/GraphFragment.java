package com.anioncode.workpower.fragment;

import android.graphics.Color;
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

public class GraphFragment  extends Fragment {
    LineChartView chartView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.graph_layout,container,false);
        chartView=view.findViewById(R.id.lineChartx);
        chartView.setGradientFillColors(new int[]{Color.parseColor("#81FFFFFF"),Color.TRANSPARENT} );

        chartView.animate();

        return view;
    }

}
