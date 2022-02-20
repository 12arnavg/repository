package com.getwellsoon.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.getwellsoon.enumeration.OutcomeType;

@Entity
@Table(name = "primary_outcome")
public class PrimaryOutcome extends Outcome {
	public PrimaryOutcome(){
		setType(OutcomeType.PRIMARY);
	}

	// @ManyToOne(fetch = FetchType.LAZY)
	// private Trial trial;

	@ManyToMany(mappedBy = "primaryOutcomes", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JsonIgnore
	private Set<Trial> trials;

	/**
	 * @return the trials
	 */
	public Set<Trial> getTrials() {
		if(trials == null) trials = new HashSet<Trial>();
		return trials;
	}


	/**
	 * @param trials the trials to set
	 */
	public void setTrials(Set<Trial> trials) {
		this.trials = trials;
	}


	@Override
	public boolean equals(Object obj) {
		boolean superEqualed = super.equals(obj);
		boolean typePrimary = (((Outcome) obj).getType() == OutcomeType.PRIMARY);
		return superEqualed && typePrimary;
	}
}
