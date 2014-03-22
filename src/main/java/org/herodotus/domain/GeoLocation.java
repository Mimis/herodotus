package org.herodotus.domain;

public class GeoLocation{
	private float longitude;
	private float latitude;
	
	public GeoLocation(float longitude, float latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "GeoLocation [longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}
	
	
}
