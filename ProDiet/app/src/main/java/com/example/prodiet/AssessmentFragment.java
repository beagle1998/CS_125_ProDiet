package com.example.prodiet;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AssessmentFragment extends Fragment {

    private TextView assessment_info;
    private TextView assessment_result;
    private TextView bmiresult;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;

    View v;
    BarChart barChart;
    private View pie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//        return inflater.inflate(R.layout.fragment_assessment1, container, false);
        v = inflater.inflate(R.layout.fragment_assessment1, container, false);

        pieChart = (PieChart) v.findViewById(R.id.pieChart);
        getEntries();
        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);


//        //////////////
        BarChart stepChart = (BarChart) v.findViewById(R.id.STEPChart);
        int stepss[] = {5000,4000,10000,7600,4000};
        ArrayList<BarEntry> stepEntries = new ArrayList<>();
        for(int i = 0 ; i<stepss.length; i++){
            stepEntries.add(new BarEntry(i,(float)stepss[i]));
        }
//        entries.add(new BarEntry(8f, 0));
//        entries.add(new BarEntry(2f, 1));
//        entries.add(new BarEntry(5f, 2));
//        entries.add(new BarEntry(20f, 3));
//        entries.add(new BarEntry(15f, 4));
//        entries.add(new BarEntry(19f, 5));

        BarDataSet stepBardataset = new BarDataSet(stepEntries, "Steps");

        BarData data = new BarData(stepBardataset);
        stepChart.setData(data); // set the data and list of labels into chart
//        barChart.setDescription("Set Bar Chart Description Here");  // set the description

        stepChart.animateY(500);
        stepChart.getXAxis().setDrawGridLines(false);//No Xaxis Grid
//        stepChart.getXAxis().setDrawLabels(false);//Delete Upper labels
        stepChart.getAxisLeft().setDrawLabels(false);//Delete Left labels
        stepChart.getAxisRight().setDrawLabels(false);
        stepChart.getAxisRight().setDrawGridLines(false);
//        stepChart.label
        Legend stepleg = stepChart.getLegend();
        stepleg.setEnabled(false);


        HorizontalBarChart bmiChart = (HorizontalBarChart) v.findViewById(R.id.BMIChart);
        BarData bmiData = new BarData(BMI());
        bmiChart.setData(bmiData);
        bmiChart.animateXY(0, 0);
        bmiChart.getXAxis().setDrawGridLines(false);


        bmiChart.getAxisLeft().setAxisMinimum(0);
        bmiChart.getAxisLeft().setAxisMaximum(50);
        bmiChart.getAxisLeft().setCenterAxisLabels(true);
        bmiChart.getAxisRight().setDrawLabels(false);
        bmiChart.getAxisRight().setDrawGridLines(false);
        Legend bmileg = bmiChart.getLegend();
        bmileg.setEnabled(false);
        bmiChart.invalidate();


        return v;
    }

    private BarDataSet BMI() {
        ArrayList<BarEntry> bmiEntries = new ArrayList();

        //BMISCALE
//        bmiEntries.add(new BarEntry(0, 50));
//        bmiEntries.add(new BarEntry(0, 30));
//        bmiEntries.add(new BarEntry(0, 25));
//        bmiEntries.add(new BarEntry(0, (float) 18.5));
        //BMISCALE

        //USER BMI INFO
        double userBMIInfo = MatchingUtils.calculateBMI(User.getHeight(), User.getWeight());
        bmiEntries.add(new BarEntry(0, (float)userBMIInfo));

//        int colors[] = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.}
        BarDataSet dataset = new BarDataSet(bmiEntries, "BMI");
        int color ;
        if(userBMIInfo <18.5){
            color = Color.GREEN;
        }else if(18.5<=userBMIInfo&&userBMIInfo<25){
            color = Color.YELLOW;
        }else if(25<=userBMIInfo&&userBMIInfo<30){
            color = Color.MAGENTA;
        }else {
            color = Color.RED;
        }

        dataset.setColors(color);

        return dataset;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        assessment_info = getView().findViewById(R.id.assessment_info);
//        assessment_result = getView().findViewById(R.id.assessment_result);
        bmiresult =(TextView)view.findViewById(R.id.textView4);
        bmiresult.setText(MatchingUtils.weightStatus(User.getHeight(),User.getWeight()));
//        assessment_info.setText(User.assessmentString());
//        assessment_result.setText("Body Status: " + MatchingUtils.weightStatus(User.getHeight(), User.getWeight()));
    }


    private void getEntries() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(3f, "BreakFast"));
        pieEntries.add(new PieEntry(3f, "Lunch"));
        pieEntries.add(new PieEntry(4f, "Dinner"));

    }
}
