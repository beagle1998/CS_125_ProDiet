package com.example.prodiet;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class AssessmentFragment extends Fragment {

    private TextView assessment_info;
    PieChart assessment_pieChart;
    private TextView assessment_BMIStatus;
    HorizontalBarChart assessment_BMIChart;

    ArrayList pieEntries;
    PieData pieData;
    PieDataSet pieDataSet;
    BarData bmiData;

    /**
     * Return fragment's view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_assessment, container, false);
    }

    /**
     * Display user's food preferences and BMI (Body Mass Index) status after creating the fragment's view
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        assessment_info = getView().findViewById(R.id.assessment_info);
        assessment_pieChart = getView().findViewById(R.id.assessment_pieChart);
        assessment_BMIStatus= getView().findViewById(R.id.assessment_BMIStatus);
        assessment_BMIChart = getView().findViewById(R.id.assessment_BMIChart);

        assessment_info.setText("User Name: "+ User.getUsername()+"\n"+User.assessmentString());
        assessment_BMIStatus.setText("Status:"+MatchingUtils.weightStatus(User.getHeight(), User.getWeight()));

        initPieChart(); // init assessment_pieChart view
        implementBMIChart(); //init assessment_BMIChart view
    }


    /**
     * Retrieve user's food history from Firebase & display history as preference in the pie chart
     */
    private void initPieChart() {
        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        Query query = fb_ref.child("History/"+User.getUsername()).orderByValue().limitToLast(5);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<?,?> history_map = (Map)dataSnapshot.getValue();
                    implementPieChart(history_map);
//                        history_map.clear();
                } else {
                    Toast.makeText(getActivity(), "You don't have any food history yet!\n(no chart data available)", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Display history as preference in the pie chart
     */
    private void implementPieChart(Map<?, ?> history_map) {

        if (pieEntries == null) {
            pieEntries = new ArrayList<>();
        } else {
            pieEntries.clear();
        }
        Iterator it = history_map.entrySet().iterator();
        while (it.hasNext()) {
            Entry pair = (Entry) it.next();
            pieEntries.add(new PieEntry((Long)pair.getValue(), pair.getKey().toString()));
        }
        pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        assessment_pieChart.setEntryLabelColor(Color.BLACK);
        pieDataSet.setValueTextSize(14);
        pieData = new PieData(pieDataSet);
        assessment_pieChart.setData(pieData);
        assessment_pieChart.setDescription(null);
        assessment_pieChart.setCenterText("Preferences of Meal");
        assessment_pieChart.setCenterTextSize(18);
        Legend pieleg = assessment_pieChart.getLegend();
        pieleg.setEnabled(false);
        assessment_pieChart.invalidate();
    }


    /**
     * Display BMI in the horizontal bar chart
     */
    private void implementBMIChart() {
        bmiData = new BarData(BMI());
        assessment_BMIChart.setData(bmiData);
        assessment_BMIChart.animateXY(0, 0);
        assessment_BMIChart.getXAxis().setDrawGridLines(false);
        assessment_BMIChart.getXAxis().setDrawLabels(false);
        assessment_BMIChart.setDescription(null);
        assessment_BMIChart.getAxisLeft().setAxisMinimum(0);
        assessment_BMIChart.getAxisLeft().setAxisMaximum(50);
        assessment_BMIChart.getAxisLeft().setCenterAxisLabels(true);
        assessment_BMIChart.getAxisRight().setDrawLabels(false);
        assessment_BMIChart.getAxisRight().setDrawGridLines(false);
        Legend bmileg = assessment_BMIChart.getLegend();
        bmileg.setEnabled(false);
        assessment_BMIChart.invalidate();
    }


    /**
     * Return user's BMI status as dataset
     */
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
        bmiEntries.add(new BarEntry(0, (float) userBMIInfo));

        BarDataSet dataset = new BarDataSet(bmiEntries, "BMI");
        int color;
        if (userBMIInfo < 18.5) {
            color = Color.GREEN;
        } else if (18.5 <= userBMIInfo && userBMIInfo < 25) {
            color = Color.YELLOW;
        } else if (25 <= userBMIInfo && userBMIInfo < 30) {
            color = Color.rgb(255, 102, 0);
        } else {
            color = Color.RED;
        }
        dataset.setColors(color);

        return dataset;
    }

}