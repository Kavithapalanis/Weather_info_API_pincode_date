package com.example.weatherinfo.modelentity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
//@Table(name = "pincode_location")
public class PincodeLocation {
	//pincode is unique & can serve directly as the primary key, simple don't need an additional id field
	//we save each pincode only once in the table

	@Id
	private String pincode;
	
	@JsonProperty("lat") // Map JSON field "lat" to this variable
    private double latitude;
	
    @JsonProperty("lon") // Map JSON field "lon" to this variable
    private double longitude;
    
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
    @Override
    public String toString() {
        return "PincodeLocation{" +
                "pincode='" + pincode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }


}
