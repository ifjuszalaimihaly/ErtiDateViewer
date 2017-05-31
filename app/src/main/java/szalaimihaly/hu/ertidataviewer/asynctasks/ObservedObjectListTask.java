package szalaimihaly.hu.ertidataviewer.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;

import szalaimihaly.hu.ertidataviewer.dataloader.ErtiDataViewer;
import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;

/**
 * Created by Mihaly on 2016.03.22..
 */
public class ObservedObjectListTask extends AsyncTask<Void,Void,ArrayList<ObservedObject>> {

    ArrayList<ObservedObject> observedObjects;

    private int beginyear;
    private int beginmonth;

    private int endyear;
    private int endmonth;

    private int beginday;
    private int endday;

    private String species;
    private String city;
    private String aggregationtype;

    public ObservedObjectListTask(int beginyear, int beginmonth, int beginday, int endyear, int endmonth, int endday, String species, String city, String aggregationtype) {
        this.beginyear = beginyear;
        this.beginmonth = beginmonth;
        this.endyear = endyear;
        this.endmonth = endmonth;
        this.beginday = beginday;
        this.species = species;
        this.endday = endday;
        this.city = city;
        this.aggregationtype = aggregationtype;
        ErtiDataViewer.changeDataLoader();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<ObservedObject> doInBackground(Void... params) {
        if(ErtiDataViewer.getDataLoader()!=null) {
            this.observedObjects = (ArrayList) ErtiDataViewer.getDataLoader().getInsectTrapBetweenDatesBySpeciesAndPlace(beginyear, beginmonth, beginday, endyear, endmonth, endday, species, city, aggregationtype);
            return this.observedObjects;
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<ObservedObject> observedObjects) {
        super.onPostExecute(observedObjects);
    }

}
