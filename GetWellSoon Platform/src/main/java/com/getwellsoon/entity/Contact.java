package com.getwellsoon.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact extends BaseEntityImpl {
	public Contact(){}

	public Contact(String phone, String name, String email){
		this.phone = phone;
		this.name = name;
		this.email = email;
	}

	private String phone;

	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	private Trial trial;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Trial getTrial() {
		return trial;
	}

	public void setTrial(Trial trial) {
		this.trial = trial;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Contact) {
			if(getId() != null
				&& ((Contact) obj).getId().equals(getId())) return true;
		}
		return false;
	}
}
