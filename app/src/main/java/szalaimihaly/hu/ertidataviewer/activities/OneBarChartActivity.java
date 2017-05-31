package szalaimihaly.hu.ertidataviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;


import java.util.ArrayList;
import java.util.Collection;

import szalaimihaly.hu.ertidataviewer.entities.InsectTrap;
import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;
import szalaimihaly.hu.ertidataviewer.R;

public class OneBarChartActivity extends BarChartAbstractActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_bar_chart);
        final Intent intent = getIntent();
        observedObjectCollections = (ArrayList<Collection>) intent.getSerializableExtra("observedobjectcollections");
        limit = intent.getIntExtra("limit", 0);
        aggregationtype = intent.getIntExtra("aggregationtype",0);
        int colorindex = intent.getIntExtra("colorindex",0);
        begindateString = intent.getStringExtra("begindate");
        enddateString = intent.getStringExtra("enddate");
        trapplacecity = intent.getStringExtra("trapplacecity");
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        barChart.setMinimumHeight(30);
        BarData barData = null;
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        for (int i = 0; i< observedObjectCollections.size(); i++){
            boolean isNotNa = true;
            ArrayList<ObservedObject> observedObjects = (ArrayList<ObservedObject>) observedObjectCollections.get(i);
            for(ObservedObject observedObject :observedObjects){
                InsectTrap insectTrap = (InsectTrap) observedObject;
                if(!insectTrap.isNA()){
                    isNotNa = false;
                }
            }
            if(!isNotNa) {
                BarDataSet dataSet = getDataSet(observedObjects);
                if (colorindex != 0) {
                    dataSet.setColor(getColors().get(colorindex));
                } else {
                    dataSet.setColor(getColors().get(i));
                }
                dataSets.add(dataSet);
            }
        }
        for (int i = 0; i < observedObjectCollections.size(); i++) {
            boolean isNotNa = true;
            ArrayList<ObservedObject> observedObjects = (ArrayList<ObservedObject>) observedObjectCollections.get(i);
            for(ObservedObject observedObject :observedObjects){
                InsectTrap insectTrap = (InsectTrap) observedObject;
                if(!insectTrap.isNA()){
                    isNotNa = false;
                }
            }
            if(!isNotNa) {
                barData = new BarData(getXValues(((ArrayList<ObservedObject>) observedObjectCollections.get(i)), aggregationtype), dataSets);
            }
        }
        barChart.setDescription("");
        barChart.setData(barData);
        summarizeTextView = (TextView) findViewById(R.id.summarizetextView);
        naTextView = (TextView) findViewById(R.id.natextView);
        naTextView.setMovementMethod(new ScrollingMovementMethod());
        writeSummarize();
        writeNaData();
        if(aggregationtype>DAY) {
                super.drillDown(barChart,-1, BarChartAbstractActivity.ONECHART);
        }
    }







}
