package szalaimihaly.hu.ertidataviewer.entities;


import java.util.ArrayList;

public class InsectTrap extends ObservedObject {

	private String speciesId;
	private String speciesName;
	private String city;
	private int trapId;


	public InsectTrap() {
		super();
		ArrayList<Double> observedValues = new ArrayList<Double>();
		ArrayList<Boolean> naValues = new ArrayList<Boolean>();
		super.setObservedValues(observedValues);
		super.setNaValues(naValues);
	}




	public InsectTrap(String speciesId, String speciesName, int catches,
					  int year, int month, int day, String city, int trapId) {
		super(year, month, day);
		ArrayList<Double> observedValues = new ArrayList<Double>();
		observedValues.add((double) catches);
		super.setObservedValues(observedValues);
		ArrayList<Boolean> naValues = new ArrayList<Boolean>();
		naValues.add(false);
		super.setNaValues(naValues);
		this.speciesId = speciesId;
		this.speciesName = speciesName;
		this.city = city;
		this.trapId = trapId;
	}

	public int getCatches(){
		double catches=super.getObservedValues().get(0);
		return  (int)catches;

	}

	public void setCatches(int catches){
		super.getObservedValues().set(0,(double)catches);
	}


	public String getSpeciesId() {
		return speciesId;
	}


	public void setSpeciesId(String speciesId) {
		this.speciesId = speciesId;
	}


	public String getSpeciesName() {
		return speciesName;
	}


	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}



	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public int getTrapId() {
		return trapId;
	}


	public void setTrapId(int trapId) {
		this.trapId = trapId;
	}


	public boolean isNA() {
		return super.getNaValues().get(0);
	}

	public void setNA() {
		super.getNaValues().set(0,true);
	}

	@Override
	public String toString() {
		return "InsectTrap [speciesId=" + speciesId + ", speciesName="
				+ speciesName + ", catches=" + getCatches() + ", year=" + super.getYear()
				+ ", month=" + super.getMonth() + ", day=" + super.getDay() + ", city=" + city
				+ ", trapId=" + trapId + ", isNA=" + super.getNaValues().get(0) + "]";
	}


}
