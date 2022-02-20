package com.getwellsoon.enumeration;

public enum OutcomeType {
	PRIMARY("primary"), SECONDARY("secondary");
	private String name;
	private OutcomeType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
