package szalaimihaly.hu.ertidataviewer.entities;


import android.location.Location;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Mihaly on 2016.02.25..
 */
public abstract class ObservedObject implements Serializable, Comparable<ObservedObject>{

    private int year;
    private int day;
    private int month;

    private int hour;
    private int minute;

    private Double latitude;
    private Double longitude;

    private List<Double> observedValues;

    private List<Boolean> naValues;

    protected ObservedObject(){

    }

    protected ObservedObject(int year, int month, int day, double latitude, double longitude){
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=0;
        this.minute=0;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected ObservedObject(int year, int month, int day){
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=0;
        this.minute=0;
        this.latitude=null;
        this.latitude=null;
    }

    protected ObservedObject(int year, int month, double latitude, double longitude){
        this.year=year;
        this.month=month;
        this.day=1;
        this.hour=0;
        this.minute=0;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    protected List<Double> getObservedValues() {
        return observedValues;
    }

    protected void setObservedValues(List<Double> observedValues) {
        this.observedValues = observedValues;
    }

    protected List<Boolean> getNaValues() {
        return naValues;
    }

    protected void setNaValues(List<Boolean> naValues) {
        this.naValues = naValues;
    }

    public Location getLocation(){
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }


    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getDateString() {
        if (this.day >= 10) {
            if (this.month >= 10) {
                return year + "-" + month + "-" + day;
            } else {
                return year + "-0" + month + "-" + day;
            }
        } else {
            if (this.month >= 10) {
                return year + "-" + month + "-0" + day;
            } else {
                return year + "-0" + month + "-0" + day;
            }
        }
    }


     /*
    public boolean isNewerThanDate(int year, int month, int day){
        if (this.year>year){
            return true;
        }
        if (this.year<year) {
            return false;
        }
        if(this.year==year){
            if(this.month>month){
                return true;
            }
            if (this.month<month){
                return false;
            }
            if (this.month==month){
                if(this.day>=day){
                    return true;
                }
                if (this.day<day){
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isOlderThanDate(int year, int month, int day){
        if (this.year<year){
            return true;
        }
        if (this.year>year) {
            return false;
        }
        if(this.year==year){
            if(this.month<month){
                return true;
            }
            if (this.month>month){
                return false;
            }
            if (this.month==month){
                if(this.day<=day){
                    return true;
                }
                if (this.day>day){
                    return false;
                }
            }
        }
        return false;
    }
    */

    @Override
    public int compareTo(ObservedObject o) {
      /*  if(this.isNewerThanDate(o.year, o.month, o.day)){
            return 1;
        }
        if(this.isOlderThanDate(o.year, o.month, o.day)){
            return -1;
        }
        return 0;
        */
        return this.getDateString().compareTo(o.getDateString());
    }


}
