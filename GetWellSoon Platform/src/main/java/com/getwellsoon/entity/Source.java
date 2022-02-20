package com.getwellsoon.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sources")
public class Source extends BaseEntityImpl {	
	private String url;
	
	public Source() {
		super();
	}
	
	public Source(String name, String url) {
		super();
		this.setName(name);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

