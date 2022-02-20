package com.getwellsoon.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "conditions")
public class Condition extends BaseEntityImpl {// implements TrialFieldMany
	public Condition() {}

	@ManyToMany(mappedBy = "conditions", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	// @JsonIgnore
	private Set<Trial> trials;

	@Lob
	@Column(name = "slug")
	private String slug;

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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
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
		if(obj instanceof Condition) {
			if(getId() != null
				&& ((Condition) obj).getId().equals(getId())) return true;
		}
		return false;
	}
}
