package com.getwellsoon.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.getwellsoon.enumeration.CriteriaType;

@Entity
@Table(name = "inclusion_criteria")
public class InclusionCriteria extends TrialCriteria {
	public InclusionCriteria() {
		super(CriteriaType.INCLUSION);
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
		boolean typePrimary = (((TrialCriteria) obj).getType() == CriteriaType.INCLUSION);
		return superEqualed && typePrimary;
	}
}
