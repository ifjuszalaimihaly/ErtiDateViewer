package szalaimihaly.hu.ertidataviewer.activities;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import szalaimihaly.hu.ertidataviewer.R;
import szalaimihaly.hu.ertidataviewer.asynctasks.ObservedObjectListTask;
import szalaimihaly.hu.ertidataviewer.dataloader.DataLoader;
import szalaimihaly.hu.ertidataviewer.entities.InsectTrap;
import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;

/**
 * Created by Mihaly on 2016.02.29..
 */
public abstract class SelectTrapPlaceAbstractActivity extends FragmentActivity {

    protected String[] speciesArray;


    protected MultiAutoCompleteTextView multiAutoCompleteTextView;
    protected Button chartButton;



    private int beginyear = 1995;
    private int beginmonth = 3;

    private int endyear = 1995;
    private int endmonth = 4;

    private int beginday = 1;
    private int endday = 30;

    private int limit;

    private boolean graficontype = true;

    private ArrayList<ArrayList> insectTrapCollections;

    protected String trapplacecity;

    public static int DAY = 0;
    public static int MONTH = 1;

    protected ArrayList<String> slice(String species) {
        ArrayList<String> speciesList = new ArrayList<String>();
        if (!species.contains(",")) {
            speciesList.add(species);
            return speciesList;
        }
        species=species.replace('.',',');
        if (!species.endsWith(", ") || !species.endsWith(",")) {
            species += ", ";
        }
        try {
            while (species.length() > 0) {
                speciesList.add(species.substring(0, species.indexOf(',')).trim());
                species = species.substring(species.indexOf(',') + 2, species.length());
            }
        } catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return speciesList;
    }

    protected void makeDiagram(String type, int aggregationtype) {
        ArrayList<String> speciesList = slice(multiAutoCompleteTextView.getText().toString());
        insectTrapCollections = new ArrayList<ArrayList>();
        Intent intent = new Intent();
        speciesList.remove("");
        ArrayList<String> speciesByCity = new ArrayList<String>(Arrays.asList(speciesArray));
        for(String s: speciesList){
            if(!speciesByCity.contains(s)){
                speciesList.remove(s);
            }
        }
        if (speciesList.size() > 3) {
            Toast.makeText(getApplicationContext(), R.string.lotspecies, Toast.LENGTH_LONG).show();
            return;
        }
        boolean isAnyNull = false;
        if (speciesList.size() != 0) {
            for (int i = 0; i < speciesList.size(); i++) {
                ArrayList<ObservedObject> insectTraps=null;
                if(aggregationtype==DAY) {
                    insectTraps= new ArrayList<ObservedObject>();
                    String beginyearString = new Integer(beginyear).toString();
                    String beginmonthString = new Integer(beginmonth).toString();
                    String begindayString = new Integer(beginday).toString();
                    Log.e("debug",beginyearString);
                    Log.e("debug",beginmonthString);
                    Log.e("debug",begindayString);
                    if(beginmonthString.length()==1){
                        beginmonthString = "0" + beginmonthString;
                    }
                    if(begindayString.length()==1){
                        begindayString =  "0" + begindayString;
                    }
                    intent.putExtra("begindate", beginyearString + "-" + beginmonthString + "-" + begindayString);
                    String endyearString = new Integer(endyear).toString();
                    String endmonthString = new Integer(endmonth).toString();
                    String enddayString = new Integer(endday).toString();
                    Log.e("debug",endyearString);
                    Log.e("debug",endmonthString);
                    Log.e("debug",enddayString);
                    if(endmonthString.length()==1){
                        endmonthString = "0" + endmonthString;
                    }
                    if(enddayString.length()==1){
                        enddayString =  "0" + enddayString;
                    }
                    intent.putExtra("enddate", endyearString + "-" + endmonthString + "-" + enddayString);
                    ObservedObjectListTask listTask = new ObservedObjectListTask(beginyear,beginmonth,beginday,endyear,endmonth,endday,speciesList.get(i),trapplacecity, DataLoader.DAY);
                    try {
                        insectTraps = listTask.execute().get();
                        if(insectTraps==null){
                            isAnyNull = true;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                }
                if(aggregationtype==MONTH){
                    insectTraps = new ArrayList<ObservedObject>();
                    String beginyearString = new Integer(beginyear).toString();
                    String beginmonthString = new Integer(beginmonth).toString();
                    if(beginmonthString.length()==1){
                        beginmonthString = "0" + beginmonthString;
                    }
                    intent.putExtra("begindate", beginyearString + "-" + beginmonthString);
                    String endyearString = new Integer(endyear).toString();
                    String endmonthString = new Integer(endmonth).toString();
                    if(endmonthString.length()==1){
                        endmonthString = "0" + endmonthString;
                    }
                    intent.putExtra("enddate", endyearString + "-" + endmonthString);
                    ObservedObjectListTask listTask = new ObservedObjectListTask(beginyear,beginmonth,1,endyear,endmonth,31,speciesList.get(i),trapplacecity,DataLoader.MONTH);
                    try {
                        insectTraps = listTask.execute().get();
                        if(insectTraps==null){
                            isAnyNull = true;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
                if(insectTraps!=null) {
                    insectTrapCollections.add(insectTraps);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.notspeciesname, Toast.LENGTH_LONG).show();
            return;
        }
        if(isAnyNull){
            Toast.makeText(getApplicationContext(),R.string.internetfail,Toast.LENGTH_LONG).show();
        }
        boolean isNotNa =false;
        for(ArrayList<ObservedObject> insectTraps: insectTrapCollections){
            for(ObservedObject observedObject: insectTraps){
                InsectTrap insectTrap = (InsectTrap) observedObject;
                if(!insectTrap.isNA()){
                    Log.e("debug","nem na");
                    isNotNa = true;
                } else {
                    Log.e("debug","na");
                }
            }
        }
        int countcharts = insectTrapCollections.size();
        if (countcharts == 0 || !isNotNa) {
            Toast.makeText(getApplicationContext(), R.string.cantdrawchart, Toast.LENGTH_LONG).show();
        } else {
            intent.putExtra("observedobjectcollections", insectTrapCollections);
            intent.putExtra("limit", limit);
            intent.putExtra("aggregationtype", aggregationtype);
            intent.putExtra("specieslist",speciesList);
            intent.putExtra("trapplacecity",trapplacecity);
            if (graficontype) {
                if (type.equals("bar")) {
                    intent.setClass(SelectTrapPlaceAbstractActivity.this, OneBarChartActivity.class);
                    startActivity(intent);
                }
            } else {
                if (countcharts <= 1) {
                    Log.e("debug", "Egy-egy grafikon");
                    if (type.equals("bar")) {
                        intent.setClass(SelectTrapPlaceAbstractActivity.this, OneBarChartActivity.class);
                        startActivity(intent);
                    }
                } else {
                    if (type.equals("bar")) {
                        intent.setClass(SelectTrapPlaceAbstractActivity.this, MoreBarChartActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }
        insectTrapCollections.clear();
    }

    public int getBeginyear() {
        return beginyear;
    }

    public void setBeginyear(int beginyear) {
        this.beginyear = beginyear;
    }

    public int getBeginmonth() {
        return beginmonth;
    }

    public void setBeginmonth(int beginmonth) {
        this.beginmonth = beginmonth;
    }

    public int getEndyear() {
        return endyear;
    }

    public void setEndyear(int endyear) {
        this.endyear = endyear;
    }

    public int getEndmonth() {
        return endmonth;
    }

    public void setEndmonth(int endmonth) {
        this.endmonth = endmonth;
    }

    public int getBeginday() {
        return beginday;
    }

    public void setBeginday(int beginday) {
        this.beginday = beginday;
    }

    public int getEndday() {
        return endday;
    }

    public void setEndday(int endday) {
        this.endday = endday;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setGraficontype(boolean graficontype) {
        this.graficontype = graficontype;
    }


}
