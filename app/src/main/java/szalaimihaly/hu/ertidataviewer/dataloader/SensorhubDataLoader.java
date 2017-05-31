package szalaimihaly.hu.ertidataviewer.dataloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import queryapi.Api;
import szalaimihaly.hu.ertidataviewer.entities.InsectTrap;
import szalaimihaly.hu.ertidataviewer.entities.MeteorologicalData;
import szalaimihaly.hu.ertidataviewer.entities.MeteorologicalPlace;
import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;
import szalaimihaly.hu.ertidataviewer.entities.SensorPlace;
import szalaimihaly.hu.ertidataviewer.entities.TrapPlace;

/**
 * Created by Mihaly on 2016.04.05..
 */
public class SensorhubDataLoader extends AbstractDataLoader {

    private Api api;

    public static int OUTER = 0;
    public static int INNER = 1;

    public SensorhubDataLoader(int connectType) {
        if(connectType == OUTER){
            api = new Api("NYMSKKSNSRHBPKYRT");
        }
        if(connectType == INNER){
            api = new Api("NYMSKKSNSRHBPKYRT", Api.INNER);
        }
    }

    @Override
    public void loadAllPlaces(int type) {

    }

    @Override
    public void loadAllSensorData(int type) {

    }

    @Override
    public void loadAll() {

    }

    @Override
    public List<SensorPlace> getAllPlaces() {
        ArrayList<SensorPlace> places = new ArrayList<SensorPlace>();
        places.addAll(getPalces(DataLoader.TRAPPLACE));
        places.addAll(getPalces(DataLoader.METEOROLOGICALPLACE));
        return places;
    }

    @Override
    public List<ObservedObject> getAllObservedObjects() {
        api.getAvailableTables();
        ArrayList<ObservedObject> observedObjects = new ArrayList<ObservedObject>();
        observedObjects.addAll(getObservedObjects(DataLoader.INSECTTRAP));
        observedObjects.addAll(getObservedObjects(DataLoader.METEOROLOGICALDATA));
        return observedObjects;
    }

    @Override
    public List<SensorPlace> getPalces(int type)  throws IllegalStateException{
        ArrayList<SensorPlace> plases = new ArrayList<SensorPlace>();
        if (type == DataLoader.METEOROLOGICALPLACE) {
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(api.getAll(getApiTables().get(3)));
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                int id = jsonObject.get("azonosito").getAsInt();
                double longitude = jsonObject.get("hosszusag").getAsDouble();
                double latitude = jsonObject.get("szelesseg").getAsDouble();
                SensorPlace sensorPlace = new MeteorologicalPlace(id, longitude, latitude);
                plases.add(sensorPlace);
            }
        }
        if (type == DataLoader.TRAPPLACE) {
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(api.getAll(getApiTables().get(0)));
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                int id = jsonObject.get("azonosito").getAsInt();
                double longitude = jsonObject.get("hosszusag").getAsDouble();
                double latitude = jsonObject.get("szelesseg").getAsDouble();
                String city = jsonObject.get("telepules").getAsString();
                SensorPlace sensorPlace = new TrapPlace(id, city, latitude, longitude);
                plases.add(sensorPlace);
            }
        }
        return plases;
    }

    @Override
    public List<ObservedObject> getObservedObjects(int type) {
        ArrayList<ObservedObject> observedObjects = new ArrayList<ObservedObject>();
        if (type == DataLoader.INSECTTRAP) {
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(api.getAll(getApiTables().get(2)));
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                int trapId = jsonObject.get("csapdaazonosito").getAsInt();
                int year = jsonObject.get("ev").getAsInt();
                int month = jsonObject.get("honap").getAsInt();
                int day = jsonObject.get("nap").getAsInt();
                int cathes = jsonObject.get("fogasszam").getAsInt();
                String speciesId = jsonObject.get("fajkod").getAsString().trim();
                String speciesName = jsonObject.get("fajnev").getAsString().trim();
                String city = jsonObject.get("telepules").getAsString().trim();
                ObservedObject observedObject = new InsectTrap(speciesId, speciesName, cathes, year, month, day, city, trapId);
                observedObjects.add(observedObject);
            }
        }
        if (type == DataLoader.METEOROLOGICALDATA) {
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(api.getAll(getApiTables().get(4)));
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                int year = jsonObject.get("ev").getAsInt();
                int month = jsonObject.get("honap").getAsInt();
                double wet = jsonObject.get("csapadek").getAsDouble();
                double temp = jsonObject.get("homerseklet").getAsDouble();
                double longitude = jsonObject.get("hosszusag").getAsDouble();
                double latitude = jsonObject.get("szelesseg").getAsDouble();
                ObservedObject observedObject = new MeteorologicalData(year, month, temp, wet, latitude, longitude);
                observedObjects.add(observedObject);
            }
        }
        return observedObjects;
    }

    @Override
    public List<String> getSpeciesByCity(String city){
        ArrayList<String> species = new ArrayList<String>();
        String[] cols = {"faj"};
        String[] columnsToCheck = {"varos"};
        String[] names = {city};
        String jsonString = api.getWithNames(getApiTables().get(5), columnsToCheck, names, cols);
        JsonArray jsonArray = (JsonArray) new JsonParser().parse(jsonString);
        for(int i = 0; i<jsonArray.size(); i++){
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            String speciesName = jsonObject.get("faj").getAsString().trim();
            species.add(speciesName);

        }
        return species;
    }

    @Override
    public List<String> getAllInsectSpecies() {
        ArrayList<String> speciesList = new ArrayList<String>();
        JsonArray jsonArray = (JsonArray) new JsonParser().parse(api.getAll(getApiTables().get(1)));
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            String species = jsonObject.get("fajnev").getAsString();
            speciesList.add(species);
        }
        return speciesList;
    }

    @Override
    public List<ObservedObject> getInsectTrapBetweenDatesBySpeciesAndPlace(int beginyear, int beginmonth, int beginday, int endyear, int endmonth, int endday, String species, String city, String aggregationType) {
        ArrayList<ObservedObject> its = new ArrayList<ObservedObject>();
            String[] cols = {"csapdaazonosito", "ev", "fajkod", "fajnev", "fogasszam", "honap", "nap", "telepules"};
            String[] columnsToCheck = {"telepules", "fajnev"};
            String[] names = {city, species};
            names[0] = String.format("%1$-" + 20 + "s", names[0]);
            names[1] = String.format("%1$-" + 40 + "s", names[1]);
            String jsonString = api.getWithNames(getApiTables().get(2), columnsToCheck, names, cols);
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(jsonString);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                int trapId = jsonObject.get("csapdaazonosito").getAsInt();
                int year = jsonObject.get("ev").getAsInt();
                int month = jsonObject.get("honap").getAsInt();
                int day = jsonObject.get("nap").getAsInt();
                int cathes = jsonObject.get("fogasszam").getAsInt();
                String speciesId = jsonObject.get("fajkod").getAsString().trim();
                String speciesName = jsonObject.get("fajnev").getAsString().trim();
                String city1 = jsonObject.get("telepules").getAsString().trim();
                ObservedObject observedObject = new InsectTrap(speciesId, speciesName, cathes, year, month, day, city1, trapId);
                InsectTrap insectTrap = (InsectTrap) observedObject;
                String dateStringBegin = makeDateSring(beginyear, beginmonth, beginday);
                String dateStringEnd = makeDateSring(endyear, endmonth, endday);
                if (insectTrap.getDateString().compareTo(dateStringBegin) >= 0 && insectTrap.getDateString().compareTo(dateStringEnd) <= 0) {
                    its.add(insectTrap);

                }
            }
            Collections.sort(its);
            its = createNA(beginyear, beginmonth, beginday, endyear, endmonth, endday, species, city, its, aggregationType);
        return its;
    }

    private ArrayList<String> getApiTables(){
        ArrayList<String> tables = new ArrayList<String>();
            String apitalbes = api.getAvailableTables();
            JsonArray jsonArray = (JsonArray) new JsonParser().parse(apitalbes);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    tables.add(entry.getKey());
                }
            }
        return tables;
    }


}
