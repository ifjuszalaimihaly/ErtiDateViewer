package szalaimihaly.hu.ertidataviewer.entities;

/**
 * Created by Mihaly on 2016.02.07..
 */
public class MeteorologicalPlace extends SensorPlace {


    public MeteorologicalPlace() {
        // TODO Auto-generated constructor stub
    }



    public MeteorologicalPlace(int id, double latitude, double longitude) {
        super(id,longitude,latitude);
    }

    @Override
    public String toString() {
        return "MeteorologicalPlace [id=" + super.getId() + ", longitude=" + super.getLongitude()
                + ", latitude=" + super.getLatitude() + "]";
    }



}
