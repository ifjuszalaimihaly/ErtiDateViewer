package szalaimihaly.hu.ertidataviewer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import szalaimihaly.hu.ertidataviewer.dataloader.DataLoader;
import szalaimihaly.hu.ertidataviewer.entities.InsectTrap;
import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;
import szalaimihaly.hu.ertidataviewer.asynctasks.ObservedObjectListTask;
import szalaimihaly.hu.ertidataviewer.R;

public abstract class BarChartAbstractActivity extends Activity {

    protected ArrayList<Collection> observedObjectCollections = new ArrayList<Collection>(3);


    protected int limit;

    protected TextView naTextView;

    protected TextView summarizeTextView;

    protected int aggregationtype;

    final static int DAY = 0;
    final static int MONTH = 1;


    public final static int ONECHART = 0;
    public final static int MORECHART = 1;


    private ArrayList<ObservedObject> its = null;


    private final Context context = this;

    protected String begindateString;
    protected String enddateString;

    protected String trapplacecity;



    protected ArrayList<BarEntry> addEntries(ArrayList<ObservedObject> observedObjects) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        if (observedObjects != null) {
            for (int i = 0; i < observedObjects.size(); i++) {
                InsectTrap insectTrap = (InsectTrap) observedObjects.get(i);
                if (insectTrap.getCatches() > limit) {
                    BarEntry entry = new BarEntry(insectTrap.getCatches(), i);
                    entries.add(entry);
                } else {
                    BarEntry entry = new BarEntry(0, i);
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    protected ArrayList<String> getXValues(ArrayList<ObservedObject> observedObjects, int aggregationtype) {
        ArrayList<String> values = new ArrayList<String>();
        if (observedObjects != null) {
            for (int i = 0; i < observedObjects.size(); i++) {
                if (aggregationtype == DAY) {
                    String month = new Integer(observedObjects.get(i).getMonth()).toString();
                    if(month.length()==1){
                        month = "0" +month;
                    }
                    String day = new Integer(observedObjects.get(i).getDay()).toString();
                    if(day.length()==1){
                        day = "0"+day;
                    }
                    values.add(month + "." + day);
                }
                if (aggregationtype == MONTH) {
                    String year = new Integer(observedObjects.get(i).getYear()).toString();
                    String month = new Integer(observedObjects.get(i).getMonth()).toString();
                    if(month.length()==1){
                        month = "0" +month;
                    }
                    values.add(year.substring(2,4) + "." + month);
                }
            }
        }
        return values;
    }

    protected ArrayList<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.argb(255,20,148,25));
        colors.add(Color.argb(255,43,25,27));
        colors.add(Color.argb(255,147,78,5));
        return colors;
    }

    protected BarDataSet getDataSet(ArrayList<ObservedObject> observedObjects) {
        BarDataSet dataSet = null;
        ArrayList<BarEntry> entries = addEntries(observedObjects);
        if (entries.size() != 0) {
            dataSet = new BarDataSet(entries, ((InsectTrap) observedObjects.get(0)).getSpeciesName());
        }
        dataSet.setDrawValues(false);
        return dataSet;
    }

    protected void writeNaData() {
        for (int i = 0; i < observedObjectCollections.size(); i++) {
            ArrayList<ObservedObject> observedObjects = (ArrayList) observedObjectCollections.get(i);
            boolean isAnyNa = false;
            boolean isNotAllNa = false;
            int size = observedObjects.size();
            int j=0;
            for (ObservedObject observedObject : observedObjects) {
                InsectTrap insectTrap = (InsectTrap) observedObject;
                if (insectTrap.isNA()) {
                    isAnyNa = true;
                    j++;
                }
            }
            isNotAllNa = (size>j);
            boolean isAnyLowerThanLimit = false;
            for (ObservedObject observedObject : observedObjects) {
                InsectTrap insectTrap = (InsectTrap) observedObject;
                if (insectTrap.getCatches() < limit) {
                    isAnyLowerThanLimit = true;
                    break;
                }
            }
            if (isNotAllNa && isAnyNa) {
                naTextView.append(((InsectTrap) observedObjects.get(0)).getSpeciesName()+"\n");
                String na = getResources().getString(R.string.na);
                naTextView.append(na+"\n");
                for (ObservedObject observedObject : observedObjects) {
                    InsectTrap insectTrap = (InsectTrap) observedObject;
                    if (insectTrap.isNA()) {
                        if(aggregationtype== BarChartAbstractActivity.DAY) {
                            naTextView.append(insectTrap.getDateString() + "\n");
                        }
                        if(aggregationtype== BarChartAbstractActivity.MONTH){
                            naTextView.append(insectTrap.getDateString().substring(0,insectTrap.getDateString().length()-3) + "\n");
                        }
                    }
                }
            }
            if (isAnyLowerThanLimit && isNotAllNa && aggregationtype == DAY) {
                naTextView.append(((InsectTrap) observedObjects.get(0)).getSpeciesName()+"\n");
                String lowerlimit = getResources().getString(R.string.lowerlimit);
                naTextView.append(lowerlimit + limit + ")\n");
                for (ObservedObject observedObject : observedObjects) {
                    InsectTrap insectTrap = (InsectTrap) observedObject;
                    if (insectTrap.getCatches() < limit) {
                        naTextView.append(insectTrap.getDateString() + ": " + insectTrap.getCatches() + "\n");
                    }
                }
            }
        }

    }

    protected void drillDown(final Chart chart, final int chartindex, final int graficontype){
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if(entry.getVal()!=0) {
                    ArrayList<InsectTrap> insectTraps = null;
                    if(graficontype == ONECHART){
                        insectTraps = (ArrayList<InsectTrap>) observedObjectCollections.get(i);
                    }
                    if (graficontype == MORECHART){
                        insectTraps = (ArrayList<InsectTrap>) observedObjectCollections.get(chartindex);
                    }

                    int index = entry.getXIndex();
                    final InsectTrap insectTrap = insectTraps.get(index);
                    int year = insectTrap.getYear();
                    int month = insectTrap.getMonth();
                    String city = insectTrap.getCity();
                    String species = insectTrap.getSpeciesName();
                    ObservedObjectListTask listTask = new ObservedObjectListTask(year, month, 1, year, month, 31, species, city, DataLoader.DAY);
                    its  = new ArrayList<ObservedObject>();
                    try {
                        its = listTask.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Collection> insecttrapcollections = new ArrayList<Collection>();
                    insecttrapcollections.add(its);
                    limit = 0;
                    final Intent drillIntent = new Intent();
                    drillIntent.putExtra("observedobjectcollections", insecttrapcollections);
                    drillIntent.putExtra("limit", limit);
                    drillIntent.putExtra("aggregationtype", BarChartAbstractActivity.DAY);
                    drillIntent.putExtra("begindate",insectTraps.get(0).getDateString());
                    drillIntent.putExtra("enddate",insectTraps.get(insectTraps.size()-1).getDateString());
                    drillIntent.putExtra("trapplacecity",trapplacecity);
                    if(chartindex==-1) {
                        drillIntent.putExtra("colorindex", i);
                    } else {
                        drillIntent.putExtra("colorindex",chartindex);
                    }
                    drillIntent.setClass(getApplicationContext(), OneBarChartActivity.class);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.drill);
                    String chooesenspecies1 = getResources().getString(R.string.choosenspecies1);
                    String period = getResources().getString(R.string.period);
                    builder.setMessage(chooesenspecies1 + " " + insectTrap.getSpeciesName() + period + " " + insectTrap.getYear() + "-" + insectTrap.getMonth());
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(its!=null) {
                                startActivity(drillIntent);
                            } else {
                                Toast.makeText(getApplicationContext(),R.string.internetfail,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            @Override
            public void onNothingSelected() {
            }
        });
    }

    protected void writeSummarize(){
        String cityString = getResources().getString(R.string.city);
        summarizeTextView.append(cityString + " " + trapplacecity+"\n");
        String period1 = getResources().getString(R.string.period1);
        summarizeTextView.append(period1 + " " + begindateString + " - " + enddateString+"\n");
        String species = getResources().getString(R.string.species);
        summarizeTextView.append(species+"\n");
        for(int i=0; i<observedObjectCollections.size(); i++){
            ArrayList<ObservedObject> observedObjects = (ArrayList<ObservedObject>) observedObjectCollections.get(i);
            int j = 0;
            for(ObservedObject observedObject: observedObjects){
                InsectTrap insectTrap = (InsectTrap) observedObject;
                if(insectTrap.isNA()){
                    j++;
                }
            }
            if(j==observedObjects.size()){
                String nodata = getResources().getString(R.string.nodata);
                summarizeTextView.append(((InsectTrap) observedObjects.get(0)).getSpeciesName()+ " " + nodata+"\n");
            } else {
                summarizeTextView.append(((InsectTrap) observedObjects.get(0)).getSpeciesName() + "\n");
            }
        }
    }

}
