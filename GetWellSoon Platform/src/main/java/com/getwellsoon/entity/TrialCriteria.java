package com.getwellsoon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import com.getwellsoon.enumeration.CriteriaType;

//import lombok.Getter;
//import lombok.Setter;

// @Entity
// @Table(name = "trial_criteria")
@MappedSuperclass
public class TrialCriteria extends BaseEntityImpl {

	public TrialCriteria(CriteriaType type) {
		this.type = type;
	}

	@Column(length = 9)
	private CriteriaType type;

	/**
	 * @return the type
	 */
	public CriteriaType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(CriteriaType type) {
		this.type = type;
	}

	
}
