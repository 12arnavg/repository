package com.getwellsoon.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "collaborator")
public class Collaborator extends BaseEntityImpl {
	public Collaborator() {
		//No intialization required.
	}

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(foreignKey = @ForeignKey(name = "fk_collaborator_trial"))
	private Trial trial;
	
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
		if(obj instanceof Collaborator) {
			if(getId() != null
				&& ((Collaborator) obj).getId().equals(getId())) return true;
		}
		return false;
	}
}
