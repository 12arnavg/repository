package com.getwellsoon.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "site_url")
public class SiteURL extends BaseEntityImpl {
	@Column(name = "url")
	private String url;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "trial_id", referencedColumnName = "id")
	private Trial trial;
	
	@Column(name = "ref_nctid")
	private String refNctId;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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

	public String getRefNctId() {
		return refNctId;
	}

	public void setRefNctId(String refNctId) {
		this.refNctId = refNctId;
	}
}
