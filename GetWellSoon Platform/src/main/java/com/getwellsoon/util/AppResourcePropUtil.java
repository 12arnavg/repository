package com.getwellsoon.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "gws.app")
@Component
// @Configuration
// @PropertySource(value = "classpath")
public class AppResourcePropUtil {
	private String serverEndpoint;
	private String trialMapping;
	private String conditionMapping;
	private String openCageApiKey;
	private String openCateApiUrl;
	
	public AppResourcePropUtil() {
	}

	/**
	 * @return the serverPath
	 */
	public String getServerEndpoint() {
		return serverEndpoint;
	}
	/**
	 * @param serverPath the serverPath to set
	 */
	public void setServerEndpoint(String serverPath) {
		this.serverEndpoint = serverPath;
	}
	/**
	 * @return the uiPath
	 */
	public String getTrialMapping() {
		return trialMapping;
	}
	/**
	 * @param uiPath the uiPath to set
	 */
	public void setTrialMapping(String uiPath) {
		this.trialMapping = uiPath;
	}

	/**
	 * @return the openCageApiKey
	 */
	public String getOpenCageApiKey() {
		return openCageApiKey;
	}

	public String getConditionMapping() {
		return conditionMapping;
	}

	public void setConditionMapping(String conditionMapping) {
		this.conditionMapping = conditionMapping;
	}

	/**
	 * @param openCageApiKey the openCageApiKey to set
	 */
	public void setOpenCageApiKey(String openCageApiKey) {
		this.openCageApiKey = openCageApiKey;
	}

	/**
	 * @return the openCateApiUrl
	 */
	public String getOpenCateApiUrl() {
		return openCateApiUrl;
	}

	/**
	 * @param openCateApiUrl the openCateApiUrl to set
	 */
	public void setOpenCateApiUrl(String openCateApiUrl) {
		this.openCateApiUrl = openCateApiUrl;
	}
	
	
}
