package com.getwellsoon.enumeration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum StudyType {
	ALL("all", 0),
	INTERVENTIONAL("Interventional", 1),
	OBSERVATIONAL("Observational", 2),
	PATIENT_REGISTRIES("Patient registries", 3),
	EXPANDED_ACCESS("Expanded Access", 4);

	private final Integer id;
	private final String label;
	private static Map<String, StudyType> typeLabelMap;

	private StudyType(String label, int id) {
		this.label = label;
		this.id = id;
	}

	static {
		typeLabelMap = new HashMap<String, StudyType>();
		for (StudyType type: values()) {
			typeLabelMap.put(type.label, type);
		}
	}

	/**
	 * Returns StudyType object by matching underlying label...
	 * @param label The String representation of the StudyType
	 * @return StudyType object
	 */
	public static StudyType getTypeByLabel(String label) {
		return typeLabelMap.get(label);
	}

	/**
	 * @return the key
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the value
	 */
	public String getLabel() {
		return label;
	}
}