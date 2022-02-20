package com.getwellsoon.enumeration;

public enum CriteriaType {
	INCLUSION("inclusion"), EXCLUSIION("exclusion");
	private String name;
	private CriteriaType(String name) {
		this.name = name;
	}
	public String getName(){
		return name;
	}
}
