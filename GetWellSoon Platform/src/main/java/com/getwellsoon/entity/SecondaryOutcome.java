package com.getwellsoon.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.getwellsoon.enumeration.OutcomeType;

@Entity
@Table(name = "secondary_outcome")
public class SecondaryOutcome extends Outcome {
	public SecondaryOutcome(){
		setType(OutcomeType.SECONDARY);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	private Trial trial;

	public void setType(OutcomeType type) {
		this.type = type;
	}

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
		boolean typePrimary = (((Outcome) obj).getType() == OutcomeType.SECONDARY);
		return superEqualed && typePrimary;
	}
}
