package szalaimihaly.hu.ertidataviewer.entities;



import java.util.ArrayList;

import szalaimihaly.hu.ertidataviewer.entities.ObservedObject;

public class MeteorologicalData extends ObservedObject {



	public MeteorologicalData() {
		super();
		ArrayList<Double> observedValues = new ArrayList<Double>();
		super.setObservedValues(observedValues);
		ArrayList<Boolean> naValues = new ArrayList<Boolean>();
		super.setNaValues(naValues);
	}



	public MeteorologicalData(int year, int month, double temp, double wet, double longitude, double latitude) {
		super(year,month,longitude,latitude);
		ArrayList<Double> observedValues = new ArrayList<Double>();
		observedValues.add(temp);
		observedValues.add(wet);
		super.setObservedValues(observedValues);
		ArrayList<Boolean> naValues = new ArrayList<Boolean>();
		naValues.add(false);
		naValues.add(false);
		super.setNaValues(naValues);
	}




	public double getTemp(){
		return super.getObservedValues().get(0);
	}

	public double getWet(){
		return super.getObservedValues().get(1);
	}


	public void setTemp(double temp){
		super.getObservedValues().set(0,temp);
	}

	public  void setWet(double wet){
		super.getObservedValues().set(1,wet);
	}

	public boolean isWetNA(){
		return super.getNaValues().get(1);
	}

	public boolean isTempNA(){
		return super.getNaValues().get(0);
	}

	public void setWetNA(){
		super.getNaValues().set(1,true);
	}

	public void setTempNA(){
		super.getNaValues().set(0,true);
	}



	@Override
	public String toString() {
		return "MeteorologicalData [year=" + super.getYear() + ", month=" + super.getMonth()
				+ ", temp=" + this.getTemp() + ", wet=" + this.getWet() + ", longitude="
				+ super.getLongitude() + ", latitude=" + super.getLatitude() + ", isTempNA=" + super.getNaValues().get(0)
				+ ", isWetNA="+ super.getNaValues().get(1)+"]";
	}



}
