package com.getwellsoon.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.getwellsoon.enumeration.CriteriaType;

@Entity
@Table(name = "exclusion_criteria")
public class ExclusionCriteria extends TrialCriteria {
	public ExclusionCriteria() {
		super(CriteriaType.EXCLUSIION);
	}

	@ManyToOne(fetch = FetchType.LAZY)
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

	@Override
	public boolean equals(Object obj) {
		boolean superEqualed = super.equals(obj);
		boolean typePrimary = (((TrialCriteria) obj).getType() == CriteriaType.EXCLUSIION);
		return superEqualed && typePrimary;
	}
}
