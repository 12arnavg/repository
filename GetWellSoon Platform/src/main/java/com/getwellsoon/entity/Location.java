package com.getwellsoon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// import com.vividsolutions.jts.geom.Geometry;
// import com.vividsolutions.jts.geom.Point;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "location")
public class Location extends BaseEntityImpl {

	@ManyToOne(fetch = FetchType.LAZY)
	private Trial trial;

	@Lob
	@Column(name = "raw_address")
	protected String rawAddress;

	@Column(name = "city")
	protected String city;

	@Column(name = "state")
	protected String state;

	@Column(name = "country")
	protected String country;

	@Column(name = "zip")
	protected String zip;

	@Type(type = "jts_geometry")
	@Column(name = "coordinates", columnDefinition = "POINT")
	protected Point coordinates;
	
	// private Double latitude;
	// private Double longitude;

	/**
	 * @return the trial
	 */
	public Trial getTrial() {
		return trial;
	}

	/**
	 * @param trial the trial to set
	 */
	public void setTrial(Trial trial) {
		this.trial = trial;
	}

	/**
	 * @return the rawAddress
	 */
	public String getRawAddress() {
		return rawAddress;
	}

	/**
	 * @param rawAddress the rawAddress to set
	 */
	public void setRawAddress(String rawAddress) {
		this.rawAddress = rawAddress;
	}

	/**
	 * @return the coordinates
	 */
	public Point getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	public Location(){}
	public Location(Point coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Location) {
			if(getId() != null
				&& ((Location) obj).getId().equals(getId())) return true;
		}
		return false;
	}
}
