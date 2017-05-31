package szalaimihaly.hu.ertidataviewer.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;

import szalaimihaly.hu.ertidataviewer.dataloader.ErtiDataViewer;

/**
 * Created by Mihaly on 2016.03.24..
 */
public class SpeciesListTask extends AsyncTask<String,Void,String[]> {



    public SpeciesListTask(){
        ErtiDataViewer.changeDataLoader();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(String... params) {
        if(ErtiDataViewer.getDataLoader()!=null) {
            String city = params[0];
            ArrayList<String> species = (ArrayList) ErtiDataViewer.getDataLoader().getSpeciesByCity(city);
            Collections.sort(species);
            String[] speciesArray = new String[species.size()];
            speciesArray = species.toArray(speciesArray);
            return speciesArray;
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
    }
}
