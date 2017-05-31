package szalaimihaly.hu.ertidataviewer.entities;


import szalaimihaly.hu.ertidataviewer.entities.SensorPlace;

public class TrapPlace extends SensorPlace {

	private String city;


	public TrapPlace() {
		// TODO Auto-generated constructor stub
	}



	public TrapPlace(int id, String city, double latitude, double longitude) {
		super(id,latitude,longitude);
		this.city = city;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	@Override
	public String toString() {
		return "TrapPlace{" +
				"id=" + super.getId() +
				", city='" + city  +
				", longitude=" + super.getLongitude()
				+ ", latitude=" + super.getLatitude() + "]";
	}
}
