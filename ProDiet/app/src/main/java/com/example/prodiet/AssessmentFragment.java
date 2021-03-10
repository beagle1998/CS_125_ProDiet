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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class AssessmentFragment extends Fragment {

    private TextView assessment_info;
    private TextView bmistatus;
    private TextView bmiresult;
    HashMap<String, Long> hash1;
    PieChart mealChart;
    ArrayList pieEntries;
    PieData pieData;
    PieDataSet pieDataSet;
    HorizontalBarChart bmiChart;
    BarData bmiData;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_assessment1, container, false);

        mealChart = (PieChart) v.findViewById(R.id.pieChart);    //create mealchart view
        implementPieChart();

        bmiChart = (HorizontalBarChart) v.findViewById(R.id.BMIChart);  //create bmichart view
        implementBMIChart();
        return v;
    }

    //thanks help processsor , api, resources,
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        assessment_info = (TextView)view.findViewById(R.id.assessment_info);
        assessment_info.setText("User Name: "+ User.getUsername()+"\n"+User.assessmentString());
        bmistatus= (TextView) view.findViewById(R.id.BMIStatus);
        bmistatus.setText("Status:");
        bmiresult = (TextView) view.findViewById(R.id.BMIResult);
        bmiresult.setText(MatchingUtils.weightStatus(User.getHeight(), User.getWeight()));
    }

    private void implementPieChart() {
        pieEntries = new ArrayList<>();
        hash1 = new HashMap<String, Long>();
        hash1.put("honey ham",  5L);
        hash1.put("corn, creamed",  4L);
        hash1.put("pumpkin soup",  4L);
        hash1.put("green chile stew",  3L);
        hash1.put("roasted corn",  2L);

        Iterator it = hash1.entrySet().iterator();
        while (it.hasNext()) {
            Entry pair = (Entry) it.next();
            String val = Long.toString((Long)pair.getValue());
            pieEntries.add(new PieEntry(Float.parseFloat(val), pair.getKey().toString()));
        }
        pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        mealChart.setEntryLabelColor(Color.BLACK);
        pieDataSet.setValueTextSize(14);
        pieData = new PieData(pieDataSet);
        mealChart.setData(pieData);
        mealChart.setDescription(null);
        mealChart.setCenterText("Preferences of Meal");
        mealChart.setCenterTextSize(18);
        Legend pieleg = mealChart.getLegend();
        pieleg.setEnabled(false);
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

    private void implementBMIChart() {
        bmiData = new BarData(BMI());
        bmiChart.setData(bmiData);
        bmiChart.animateXY(0, 0);
        bmiChart.getXAxis().setDrawGridLines(false);
        bmiChart.getXAxis().setDrawLabels(false);
        bmiChart.setDescription(null);
        bmiChart.getAxisLeft().setAxisMinimum(0);
        bmiChart.getAxisLeft().setAxisMaximum(50);
        bmiChart.getAxisLeft().setCenterAxisLabels(true);
        bmiChart.getAxisRight().setDrawLabels(false);
        bmiChart.getAxisRight().setDrawGridLines(false);
        Legend bmileg = bmiChart.getLegend();
        bmileg.setEnabled(false);
        bmiChart.invalidate();
    }
//private void test(){
//     DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
//    fb_ref.child("History").child(User.getUsername()).addValueEventListener(new ValueEventListener() {
//
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//            String top1="",top2="",top3="";
//            Long top11=0L,top22=0L,top33=0L;
//            int size = (int) dataSnapshot.getChildrenCount();
//            System.out.println("Size===="+size+"\n"+dataSnapshot.getValue());
//
//            HashMap<String,Long> hash = (HashMap<String, Long>) dataSnapshot.getValue();
//            Iterator it = hash.entrySet().iterator();
//            while(it.hasNext()){
//                Entry pair = (Entry)it.next();
//                System.out.println(pair.getKey()+ " = " + pair.getValue());
//                if((Long)pair.getValue()>top11){
//                    top33= top22;
//                    top22 = top11;
//                    top11 = (Long)pair.getValue();
//                    top1 = pair.getKey().toString();
//                }
//                else if ((Long)pair.getValue()>top22){
//                    top33 = top22;
//                    top22 = (Long)pair.getValue();
//                    top2 = pair.getKey().toString();
//                }else if((Long)pair.getValue()>top33){
//                    top33 = (Long)pair.getValue();
//                    top3 = pair.getKey().toString();
//                }
//
//            }
//
//            System.out.println("Top1:"+top1+"\nTop2:"+top2+"\nTop3:"+top3); //SUCCESS
//            result.put(top1,top11);
//            result.put(top2,top22);
//            result.put(top3,top33);
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });
//    }


}
