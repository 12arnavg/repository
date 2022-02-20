package com.getwellsoon.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.getwellsoon.enumeration.OutcomeType;

@MappedSuperclass
public class Outcome extends BaseEntityImpl {
	@Column
	protected OutcomeType type;

	// @ManyToOne(fetch = FetchType.LAZY)
	// protected Trial trial;

	public OutcomeType getType() {
		return type;
	}

	public void setType(OutcomeType type) {
		this.type = type;
	}

	// /**
	//  * @return the trial
	//  */
	// public Trial getTrial() {
	// 	return trial;
	// }

	// /**
	//  * @param trial the trial to set
	//  */
	// public void setTrial(Trial trial) {
	// 	this.trial = trial;
	// }

	/**
	 * Always return hashcode of 'this' class and not its super implementation.
	 */
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	/**
	 * Equality should be done against the id (PK) of this class.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Outcome) {
			if(getId() != null
				&& ((Outcome) obj).getId().equals(getId())) return true;
		}
		return false;
	}
}
