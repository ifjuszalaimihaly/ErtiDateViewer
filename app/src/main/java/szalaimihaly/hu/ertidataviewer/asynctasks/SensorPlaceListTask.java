package szalaimihaly.hu.ertidataviewer.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;

import szalaimihaly.hu.ertidataviewer.dataloader.ErtiDataViewer;
import szalaimihaly.hu.ertidataviewer.entities.SensorPlace;

/**
 * Created by Mihaly on 2016.03.22..
 */
public class SensorPlaceListTask extends AsyncTask<Integer,Void, ArrayList<SensorPlace>> {

    public SensorPlaceListTask(){
        ErtiDataViewer.changeDataLoader();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<SensorPlace> doInBackground(Integer... params) {
        int tpye = params[0];
        if(ErtiDataViewer.getDataLoader()!=null) {
            return (ArrayList) ErtiDataViewer.getDataLoader().getPalces(tpye);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<SensorPlace> sensorPlaces) {
        super.onPostExecute(sensorPlaces);
    }


}
