package szalaimihaly.hu.ertidataviewer.entities;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Mihaly on 2016.02.25..
 */
public abstract class SensorPlace implements Serializable{

    private int id;
    private Double latitude;
    private Double longitude;

    protected SensorPlace(){

    }

    protected SensorPlace(int id, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Location getLocation(){
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
