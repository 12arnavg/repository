package com.getwellsoon.model;

public class FilterTrialTO {
	private String gender;
	private Integer minAge;
	private Integer maxAge;
	private Integer age;
	private String name;
	private LocationTO location;
	private Integer pageNumber;
	private Integer batchSize;
	private String slug;

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	
	/**
	 * @return the minAge
	 */
	public Integer getMinAge() {
		return minAge;
	}
	/**
	 * @param minAge the minAge to set
	 */
	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}
	/**
	 * @return the maxAge
	 */
	public Integer getMaxAge() {
		return maxAge;
	}
	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the location
	 */
	public LocationTO getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(LocationTO location) {
		this.location = location;
	}
	/**
	 * @return the pageNumber
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the batchSize
	 */
	public Integer getBatchSize() {
		return batchSize;
	}
	/**
	 * @param batchSize the batchSize to set
	 */
	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public FilterTrialTO() {}

	/**
	 * @param gender
	 * @param age
	 * @param name
	 * @param pageNumber
	 * @param batchSize
	 */
	public FilterTrialTO(String gender, Integer age, String name, Integer pageNumber, Integer batchSize) {
		this.gender = gender;
		this.age = age;
		this.name = name;
		this.pageNumber = pageNumber;
		this.batchSize = batchSize;
	}
}
