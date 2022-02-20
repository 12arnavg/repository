package com.getwellsoon.util;

import java.util.ArrayList;
import java.util.List;

import com.getwellsoon.enumeration.EnrollmentStatus;

public class TrialHelper {
	public static final List<String> activeTrialStatus () {
		final List<String> statusList = new ArrayList<String> ();
		statusList.add(EnrollmentStatus.NOT_YET_RECRUITING.getLabel());
		statusList.add(EnrollmentStatus.RECRUITING.getLabel());
		statusList.add(EnrollmentStatus.ENROLLING_BY_INVITATION.getLabel());
		statusList.add(EnrollmentStatus.ACTIVE_NOT_RECRUITING.getLabel());
		return statusList;
	}
}
