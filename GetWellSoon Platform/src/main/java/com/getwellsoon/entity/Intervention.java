package com.getwellsoon.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "intervention")
public class Intervention extends BaseEntityImpl {
	public Intervention() {}

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
		if(obj == null) return false;
		if(obj instanceof Intervention) {
			if(getId() != null
				&& ((Intervention) obj).getId().equals(getId())) return true;
		}
		return false;
	}
}
