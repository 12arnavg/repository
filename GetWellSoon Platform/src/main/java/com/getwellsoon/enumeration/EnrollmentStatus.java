package com.getwellsoon.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum EnrollmentStatus {
	NOT_YET_RECRUITING ("Not yet recruiting"),
	RECRUITING ("Recruiting"),
	ENROLLING_BY_INVITATION ("Enrolling by invitation"),
	ACTIVE_NOT_RECRUITING ("Active, not recruiting"),
	SUSPENDED ("Suspended"),
	TERMINATED ("Terminated"),
	COMPLETED ("Completed"),
	WITHDRAWN ("Withdrawn"),
	UNKNOWN  ("Unknown ");

	private final String label;
	private static Map<String, EnrollmentStatus> statusLabelMap;
	
	private EnrollmentStatus(String label) {
		this.label = label;
	}

	static {
		statusLabelMap = new HashMap<String, EnrollmentStatus>();
		for (EnrollmentStatus status : values()) {
			statusLabelMap.put(status.label, status);
		}
	}

	public static EnrollmentStatus getStatusByLabel(String label) {
		return statusLabelMap.get(label);
	}

	public String getLabel() {
		return label;
	}
}