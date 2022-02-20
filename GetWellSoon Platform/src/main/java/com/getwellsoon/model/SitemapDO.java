package com.getwellsoon.model;

public class SitemapDO {
	private String trialId;
	private String pathParamValue;
	private String processedAtDate;
	private String enrollmentStatus;

	/**
	 * @param trialId
	 * @param processedAtDate
	 */
	public SitemapDO(String trialId, String pathParamValue, String processedAtDate, String enrollmentStatus) {
		this.trialId = trialId;
		this.pathParamValue = pathParamValue;
		this.processedAtDate = processedAtDate;
		this.enrollmentStatus = enrollmentStatus;
	}

	public SitemapDO(String pathParamValue) {
		this.pathParamValue = pathParamValue;
	}

	/**
	 * @return the trialId
	 */
	public String getTrialId() {
		return trialId;
	}
	/**
	 * @param trialId the trialId to set
	 */
	public void setTrialId(String trialId) {
		this.trialId = trialId;
	}

	/**
	 * @return the nctId
	 */
	public String getPathParamValue() {
		return pathParamValue;
	}
	/**
	 * @param nctId the nctId to set
	 */
	public void setPathParamValue(String nctId) {
		this.pathParamValue = nctId;
	}

	/**
	 * @return the lastModDate
	 */
	public String getProcessedAtDate() {
		return processedAtDate;
	}
	/**
	 * @param processedAtDate the lastModDate to set
	 */
	public void setProcessedAtDate(String processedAtDate) {
		this.processedAtDate = processedAtDate;
	}
	
	/**
	 * @return the enrollmentStatus
	 */
	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}
	/**
	 * @param enrollmentStatus use to find whether trial is active or not for priority tag
	 */
	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}
	
}
