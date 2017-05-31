package szalaimihaly.hu.ertidataviewer.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

import java.util.ArrayList;
import java.util.Collection;

import szalaimihaly.hu.ertidataviewer.R;
import szalaimihaly.hu.ertidataviewer.entities.InsectTrap;
import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;

public class MoreBarChartActivity extends BarChartAbstractActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_bar_chart);
        Intent intent = getIntent();
        observedObjectCollections = (ArrayList<Collection>) intent.getSerializableExtra("observedobjectcollections");
        limit = intent.getIntExtra("limit", 0);
        aggregationtype = intent.getIntExtra("aggregationtype", 0);
        begindateString = intent.getStringExtra("begindate");
        enddateString = intent.getStringExtra("enddate");
        trapplacecity = intent.getStringExtra("trapplacecity");
        ArrayList<Chart> charts = (ArrayList) createCharts();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.chartList);
        for(int i=0; i<charts.size(); i++){
            Chart chart = charts.get(i);
            linearLayout.addView(chart);
        }
        summarizeTextView = (TextView) findViewById(R.id.summarizetextView);
        naTextView = (TextView) findViewById(R.id.naTextViewMoreChart);
        naTextView.setMovementMethod(new ScrollingMovementMethod());
        writeSummarize();
        writeNaData();


    }


    private ArrayList<BarChart> createCharts() {
        ArrayList<BarChart> charts = new ArrayList<BarChart>();
        for (int i = 0; i < observedObjectCollections.size(); i++) {
            ArrayList<ObservedObject> observedObjects = (ArrayList<ObservedObject>) observedObjectCollections.get(i);
            boolean isNotNa = true;
            for (ObservedObject observedObject : observedObjects) {
                InsectTrap insectTrap = (InsectTrap) observedObject;
                if (!insectTrap.isNA()) {
                    isNotNa = false;
                }
            }
            if(!isNotNa) {
                BarChart chart = new BarChart(getApplicationContext());
                BarDataSet dataSet = getDataSet((ArrayList) observedObjectCollections.get(i));
                dataSet.setColor(getColors().get(i));
                BarData barData = new BarData(getXValues(((ArrayList) observedObjectCollections.get(i)), aggregationtype), dataSet);
                chart.setDescription("");
                DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
                int px = Math.round(300 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                chart.setMinimumHeight(px);
                chart.setData(barData);
                if (aggregationtype > DAY) {
                    super.drillDown(chart, i,BarChartAbstractActivity.MORECHART);
                }
                charts.add(chart);
            }
        }
        return charts;
    }

}
