package com.getwellsoon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ConditionTO {
	private Long id;
	private String name;
	private Long trialCount;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the trialCount
	 */
	public Long getTrialCount() {
		return trialCount;
	}
	/**
	 * @param trialCount the trialCount to set
	 */
	public void setTrialCount(Long trialCount) {
		this.trialCount = trialCount;
	}
	public ConditionTO() {}
	public ConditionTO(Long id, String name, Long trialCount) {
		this.id = id;
		this.name = name;
		this.trialCount = trialCount;
	}
}
