package com.getwellsoon.enumeration;

public enum StudyPhase {
	EARLY_PHASE ("Early phase", 0),
	PHASE_1("Phase 1", 1),
	PHASE_2("Phase 2", 2),
	PHASE_3("Phase 3", 3),
	PHASE_4("Phase 4", 4),
	NOT_APPLICABLE("Not Applicable", 5);

	private final String key;
	private final Integer value;

	private StudyPhase (String key, Integer value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
}