package szalaimihaly.hu.ertidataviewer.dataloader;

import java.util.List;

import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;
import szalaimihaly.hu.ertidataviewer.entities.SensorPlace;

public interface DataLoader {


    int TRAPPLACE = 1;
    int METEOROLOGICALPLACE = 2;
    int INSECTTRAP = 3;
    int METEOROLOGICALDATA = 4;

    String DAY = "DAY";
    String MONTH= "MONTH";

    void loadAllPlaces(int type);

    void loadAllSensorData(int type);

    void loadAll();

    List<SensorPlace> getAllPlaces();

    List<ObservedObject> getAllObservedObjects();

    List<SensorPlace> getPalces(int type);

    List<ObservedObject> getObservedObjects(int type);

    List<String> getAllInsectSpecies();

    List<String> getSpeciesByCity(String city);

    List<ObservedObject> getInsectTrapBetweenDatesBySpeciesAndPlace(int beginyear, int beginmonth, int beginday,
                                                                    int endyear, int endmonth, int endday, String species,
                                                                    String city, String aggregationType);



}
