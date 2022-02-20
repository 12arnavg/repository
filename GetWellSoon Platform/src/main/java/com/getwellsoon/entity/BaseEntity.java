package com.getwellsoon.entity;

import java.util.Date;

public interface BaseEntity {

	Long getId();

	void setId(Long id);

	String getName();

	void setName(String name);

	/**
	 * @return the createdDate
	 */
	Date getCreatedDate();

	/**
	 * @param createdDate the createdDate to set
	 */
	void setCreatedDate(Date createdDate);

	/**
	 * @return the modifiedDate
	 */
	Date getModifiedDate();

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	void setModifiedDate(Date modifiedDate);

}