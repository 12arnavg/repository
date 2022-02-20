package com.getwellsoon.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.getwellsoon.entity.BaseEntity;
import com.getwellsoon.entity.BaseEntityImpl;
import com.getwellsoon.entity.Collaborator;
import com.getwellsoon.entity.Condition;
import com.getwellsoon.entity.Contact;
import com.getwellsoon.entity.Intervention;
import com.getwellsoon.entity.Investigator;
import com.getwellsoon.entity.Location;
import com.getwellsoon.entity.OtherStudy;
import com.getwellsoon.entity.PrimaryOutcome;
import com.getwellsoon.entity.Publication;
import com.getwellsoon.entity.ResponsibleParty;
import com.getwellsoon.entity.SecondaryOutcome;
import com.getwellsoon.entity.StudySponsor;
import com.getwellsoon.entity.Trial;
import com.getwellsoon.entity.TrialField;
import com.getwellsoon.enumeration.StudyType;
import com.getwellsoon.util.ListSetUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_EMPTY)
public class TrialMessage extends BaseEntityImpl {
	
	private Date createdAt;

	private Date processedAt;

	private Boolean isDeleted;
	
	private String firstPosted;
	
	private String firstSubmitted;
	
	private String lastUpdated;
	
	private String studyStart;
	
	private String studyCompletion;
	
	private List<String> primaryOutcomes;
	
	private List<String> secondaryOutcomes;
	
	private String briefTitle;
	
	private String officialTitle;
	
	private String summary;
	
	private String description;
	
	private String studyType;
	
	private String studyPhase;
	
	private String studyDesign;
	
	private List<String> conditions;
	
	private List<String> interventions;
	
	private List<String> publications;

	private String enrollmentEstimate;

	private String lobText;
	
	private String enrollmentStatus;
	
	private Integer enrollmentCount;
	
	private String gender;
	
	private Integer minAge;
	
	private Integer maxAge;
	
	private List<String> contacts;
	
	private String nctId;

	private List<String> responsibleParty;
	
	private List<String> studySponsor;

	private List<String> collaborators;
	
	private List<String> investigators;

	private List<String> otherStudyId;

	private Long sourceId;

	// private String siteId;

	private String url;

	private List<LocationTO> locations;

	private List<String> inclusionCriterias;

	private List<String> exclusionCriterias;
	
	private Double distance;

	private String conditionName;

	private String urlNctId;

	public TrialMessage() {
		super();
	}

	/**
	 * 
	 * @param id
	//  * @param siteId
	 * @param nctId
	 * @param min
	 * @param max
	 * @param gender
	 * @param briefTitle
	 * @param summary
	 * @param studyStart
	 * @param lastUpdated
	 */
	public TrialMessage (Long id, /* UUID siteId, */ String nctId,
				Integer min, Integer max, String gender,
				String briefTitle, String summary, String studyStart, String lastUpdated) {
		this(id, /* siteId,  */nctId, min, max, gender, briefTitle, summary, studyStart, lastUpdated, 0d, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param id
	//  * @param siteId
	 * @param nctId
	 * @param min
	 * @param max
	 * @param gender
	 * @param briefTitle
	 * @param summary
	 * @param studyStart
	 * @param lastUpdated
	 * @param distance
	 */
	public TrialMessage (Long id, /* UUID siteId, */ String nctId,
				Integer min, Integer max, String gender,
				String briefTitle, String summary, String studyStart, String lastUpdated, Double distance) {
		this(id, /* siteId, */ nctId, min, max, gender, briefTitle, summary, studyStart, lastUpdated, distance, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param id
	//  * @param siteId
	 * @param nctId
	 * @param min
	 * @param max
	 * @param gender
	 * @param briefTitle
	 * @param summary
	 * @param studyStart
	 * @param lastUpdated
	 * @param distance
	 * @param address
	 */
	public TrialMessage (Long id, /* UUID siteId, */ String nctId,
				Integer min, Integer max, String gender,
				String briefTitle, String summary, String studyStart, String lastUpdated, Double distance, String address, String city, String state, String country, String zipCode, String conditionName) {	//, , List<String> condition
		this.id = id;
		// this.siteId = siteId.toString();
		this.nctId = nctId;
		this.minAge = min;
		this.maxAge = max;
		this.gender = gender;
		this.briefTitle = briefTitle;
		this.summary = summary;
		this.studyStart = studyStart;
		this.conditionName = conditionName;

		if(address == null) return;
		List<LocationTO> locationList = new ArrayList<LocationTO>();
		LocationTO l = new LocationTO();
		l.setAddress(address);
		l.setCity(city);
		l.setState(state);
		l.setCountry(country);
		l.setZip(zipCode);
		locationList.add(l);
		this.locations = locationList;// ListSetUtils.locationListToLocationTOList(locations);
		this.distance = distance;
		this.lastUpdated = lastUpdated;
		// this.conditions = condition;// Arrays.asList(condition);
	}

	public static TrialMessage copy(Trial source, boolean partial) {
		TrialMessage destination = new TrialMessage();
		destination.id = source.getId();
		destination.summary = source.getSummary();
		destination.briefTitle = source.getBriefTitle();
		// destination.siteId = source.getSiteId().toString();
		return destination;
	}
	
	public static TrialMessage copy(Trial source) {
		TrialMessage destination = new TrialMessage();
		// destination.siteId = source.getSiteId() != null ? source.getSiteId().toString() : "";
		destination.processedAt = source.getProcessedAt();
		destination.isDeleted = source.getIsDeleted();
		destination.firstPosted = source.getFirstPosted();
		destination.lastUpdated = source.getLastUpdated();
		destination.studyStart = source.getStudyStart();
		destination.studySponsor = setToList(source.getStudySponsors());
		destination.studyCompletion = source.getStudyCompletion();
		destination.primaryOutcomes = setToList(source.getPrimaryOutcomes());
		destination.secondaryOutcomes = setToList(source.getSecondaryOutcomes());
		destination.briefTitle = source.getBriefTitle();
		destination.officialTitle = source.getOfficialTitle();
		destination.studyType = StringUtils.isEmpty(source.getStudyType()) ? "" : source.getStudyType().getLabel();
		destination.studyPhase = source.getStudyPhase();
		destination.studyDesign = source.getStudyDesign();
		destination.conditions = setToList(source.getConditions());
		destination.interventions = setToList(source.getInterventions());
		destination.publications = setToList(source.getPublications());
		destination.enrollmentCount = source.getEnrollmentCount();
		destination.inclusionCriterias = setToList(source.getInclusionCriterias());
		destination.exclusionCriterias = setToList(source.getExclusionCriterias());
		destination.gender = source.getGender();
		destination.minAge = source.getMinAge();
		destination.maxAge = source.getMaxAge();
		destination.contacts = setToList(source.getContacts());
		destination.locations = setToLocationList(source.getLocations());
		destination.nctId = source.getNctId();
		destination.responsibleParty = setToList(source.getResponsibleParties());
		destination.collaborators = setToList(source.getCollaborators()); //listToString(source.getCollaborators());
		destination.investigators = setToList(source.getInvestigators()); //listToString(source.getInvestigators());
		destination.interventions = setToList(source.getInterventions());
		destination.enrollmentStatus = source.getEnrollmentStatus();
		destination.summary = source.getSummary();
		destination.description = source.getDescription();
		// destination.otherStudyId = setToList(source.getOtherStudies());
		destination.enrollmentEstimate = source.getEnrollmentEstimate();
		return destination;
	}

	/**
	 * Prepare a Trial entity object and populate it with the model <code>from</code>
	 * @param from the TrialMessage object
	 * @return Trial object
	 */
	public static Trial preparedEntity(TrialMessage from) {
		Trial newTrial = new Trial();
		return preparedEntity(newTrial, from);
	}

	/**
	 * Prepare a Trial entity object and populate it with the model <code>from</code>
	 * @param entity either a new or a persisted Trial object
	 * @param from the TrialMessage object
	 * @return Trial object
	 */
	public static Trial preparedEntity(Trial entity, TrialMessage from) {
		// if(entity.getId() == null)
		// 	entity.setSiteId(null);
		entity.setProcessedAt(new Date());
		entity.setIsDeleted(from.getIsDeleted());
		entity.setFirstPosted(from.getFirstPosted());
		entity.setLastUpdated(from.getLastUpdated());
		entity.setStudyStart(from.getStudyStart());
		entity.setStudyCompletion(from.getStudyCompletion());
		entity.setBriefTitle(from.getBriefTitle());
		entity.setOfficialTitle(from.getOfficialTitle());
		entity.setStudyType(StudyType.getTypeByLabel(from.getStudyType()));
		entity.setStudyPhase(from.getStudyPhase());
		entity.setStudyDesign(from.getStudyDesign());
		entity.setGender(from.getGender());
		entity.setMinAge(from.getMinAge());
		entity.setMaxAge(from.getMaxAge());
		entity.setNctId(from.getNctId());
		entity.setSummary(from.getSummary());
		entity.setDescription(from.getDescription());
		entity.setEnrollmentEstimate(from.getEnrollmentEstimate());

		// entity.setPrimaryOutcomes( (Set<PrimaryOutcome>) listToSet( from.getPrimaryOutcomes(), PrimaryOutcome.class) );
		// entity.setSecondaryOutcomes( (Set<SecondaryOutcome>) listToSet( from.getSecondaryOutcomes(), SecondaryOutcome.class) );
		// entity.setStudySponsors( (Set<StudySponsor>) listToSet(from.getStudySponsor(), StudySponsor.class) );
		// entity.setConditions( (Set<Condition>) listToSet(from.getConditions(), Condition.class) );
		// entity.setInterventions( (Set<Intervention>) listToSet(from.getInterventions(), Intervention.class) );
		// entity.setPublications( (Set<Publication>) listToSet(from.getPublications(), Publication.class) );
		// entity.setContacts( (Set<Contact>) listToSet(from.getContacts(), Contact.class) );
		// entity.setResponsibleParties( (Set<ResponsibleParty>) listToSet(from.getResponsibleParty(), ResponsibleParty.class) );
		// entity.setCollaborators( (Set<Collaborator>) listToSet(from.getCollaborators(), Collaborator.class) );
		// entity.setInvestigators( (Set<Investigator>) listToSet(from.getInvestigators(), Investigator.class) );

		entity.setEnrollmentStatus(from.getEnrollmentStatus());
		entity.setEnrollmentCount(from.getEnrollmentCount());
		// this.inclusion_criteria = fromDelimSeparatedString(trial.getInclusion_criteria());
		// this.exclusion_criteria = fromDelimSeparatedString(trial.getExclusion_criteria());
		// entity.setLocations(stringToList(from.getLocations()));
		return entity;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the processedAt
	 */
	public Date getProcessedAt() {
		return processedAt;
	}

	/**
	 * @param processedAt the processedAt to set
	 */
	public void setProcessedAt(Date processedAt) {
		this.processedAt = processedAt;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the firstPosted
	 */
	public String getFirstPosted() {
		return firstPosted;
	}

	/**
	 * @param firstPosted the firstPosted to set
	 */
	public void setFirstPosted(String firstPosted) {
		this.firstPosted = firstPosted;
	}

	/**
	 * @return the firstSubmitted
	 */
	public String getFirstSubmitted() {
		return firstSubmitted;
	}

	/**
	 * @param firstSubmitted the firstSubmitted to set
	 */
	public void setFirstSubmitted(String firstSubmitted) {
		this.firstSubmitted = firstSubmitted;
	}

	/**
	 * @return the lastUpdated
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the studyStart
	 */
	public String getStudyStart() {
		return studyStart;
	}

	/**
	 * @param studyStart the studyStart to set
	 */
	public void setStudyStart(String studyStart) {
		this.studyStart = studyStart;
	}

	/**
	 * @return the studyCompletion
	 */
	public String getStudyCompletion() {
		return studyCompletion;
	}

	/**
	 * @param studyCompletion the studyCompletion to set
	 */
	public void setStudyCompletion(String studyCompletion) {
		this.studyCompletion = studyCompletion;
	}

	/**
	 * @return the primaryOutcomes
	 */
	public List<String> getPrimaryOutcomes() {
		return primaryOutcomes;
	}

	/**
	 * @param primaryOutcomes the primaryOutcomes to set
	 */
	public void setPrimaryOutcomes(List<String> primaryOutcomes) {
		this.primaryOutcomes = primaryOutcomes;
	}

	/**
	 * @return the secondaryOutcomes
	 */
	public List<String> getSecondaryOutcomes() {
		return secondaryOutcomes;
	}

	/**
	 * @param secondaryOutcomes the secondaryOutcomes to set
	 */
	public void setSecondaryOutcomes(List<String> secondaryOutcomes) {
		this.secondaryOutcomes = secondaryOutcomes;
	}

	/**
	 * @return the briefTitle
	 */
	public String getBriefTitle() {
		return briefTitle;
	}

	/**
	 * @param briefTitle the briefTitle to set
	 */
	public void setBriefTitle(String briefTitle) {
		this.briefTitle = briefTitle;
	}

	/**
	 * @return the officialTitle
	 */
	public String getOfficialTitle() {
		return officialTitle;
	}

	/**
	 * @param officialTitle the officialTitle to set
	 */
	public void setOfficialTitle(String officialTitle) {
		this.officialTitle = officialTitle;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the studyType
	 */
	public String getStudyType() {
		return studyType;
	}

	/**
	 * @param studyType the studyType to set
	 */
	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	/**
	 * @return the studyPhase
	 */
	public String getStudyPhase() {
		return studyPhase;
	}

	/**
	 * @param studyPhase the studyPhase to set
	 */
	public void setStudyPhase(String studyPhase) {
		this.studyPhase = studyPhase;
	}

	/**
	 * @return the studyDesign
	 */
	public String getStudyDesign() {
		return studyDesign;
	}

	/**
	 * @param studyDesign the studyDesign to set
	 */
	public void setStudyDesign(String studyDesign) {
		this.studyDesign = studyDesign;
	}

	/**
	 * @return the conditions
	 */
	public List<String> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}	

	/**
	 * @return the interventions
	 */
	public List<String> getInterventions() {
		return interventions;
	}

	/**
	 * @param interventions the interventions to set
	 */
	public void setInterventions(List<String> interventions) {
		this.interventions = interventions;
	}

	/**
	 * @return the publications
	 */
	public List<String> getPublications() {
		return publications;
	}

	/**
	 * @param publications the publications to set
	 */
	public void setPublications(List<String> publications) {
		this.publications = publications;
	}

	/**
	 * @return the enrollmentEstimate
	 */
	public String getEnrollmentEstimate() {
		return enrollmentEstimate;
	}

	/**
	 * @param enrollmentEstimate the enrollmentEstimate to set
	 */
	public void setEnrollmentEstimate(String enrollmentEstimate) {
		this.enrollmentEstimate = enrollmentEstimate;
	}

	/**
	 * @return the enrollmentStatus
	 */
	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}

	/**
	 * @param enrollmentStatus the enrollmentStatus to set
	 */
	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}
	
	/**
	 * @return the lobText
	 */
	public String getLobText() {
		return lobText;
	}

	/**
	 * @param lobText the lobText to set
	 */
	public void setLobText(String lobText) {
		this.lobText = lobText;
	}

	/**
	 * @return the enrollmentCount
	 */
	public Integer getEnrollmentCount() {
		return enrollmentCount;
	}

	/**
	 * @param enrollmentCount the enrollmentCount to set
	 */
	public void setEnrollmentCount(Integer enrollmentCount) {
		this.enrollmentCount = enrollmentCount;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the minAge
	 */
	public Integer getMinAge() {
		return minAge;
	}

	/**
	 * @param minAge the minAge to set
	 */
	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	/**
	 * @return the maxAge
	 */
	public Integer getMaxAge() {
		return maxAge;
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	/**
	 * @return the contacts
	 */
	public List<String> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the locations
	 */
	public List<LocationTO> getLocations() {
		return locations;
	}

	/**
	 * @param locations the locations to set
	 */
	public void setLocations(List<LocationTO> locations) {
		this.locations = locations;
	}

	/**
	 * @return the inclusionCriterias
	 */
	public List<String> getInclusionCriterias() {
		return inclusionCriterias;
	}

	/**
	 * @param inclusionCriterias the inclusionCriterias to set
	 */
	public void setInclusionCriterias(List<String> inclusionCriterias) {
		this.inclusionCriterias = inclusionCriterias;
	}

	/**
	 * @return the exclusionCriterias
	 */
	public List<String> getExclusionCriterias() {
		return exclusionCriterias;
	}

	/**
	 * @param exclusionCriterias the exclusionCriterias to set
	 */
	public void setExclusionCriterias(List<String> exclusionCriterias) {
		this.exclusionCriterias = exclusionCriterias;
	}

	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	/**
	 * @return the nctId
	 */
	public String getNctId() {
		return nctId;
	}

	/**
	 * @param nctId the nctId to set
	 */
	public void setNctId(String nctId) {
		this.nctId = nctId;
	}

	/**
	 * @return the responsibleParty
	 */
	public List<String> getResponsibleParty() {
		return responsibleParty;
	}

	/**
	 * @param responsibleParty the responsibleParty to set
	 */
	public void setResponsibleParty(List<String> responsibleParty) {
		this.responsibleParty = responsibleParty;
	}

	/**
	 * @return the studySponsor
	 */
	public List<String> getStudySponsor() {
		return studySponsor;
	}

	/**
	 * @param studySponsor the studySponsor to set
	 */
	public void setStudySponsor(List<String> studySponsor) {
		this.studySponsor = studySponsor;
	}

	/**
	 * @return the collaborators
	 */
	public List<String> getCollaborators() {
		return collaborators;
	}

	/**
	 * @param collaborators the collaborators to set
	 */
	public void setCollaborators(List<String> collaborators) {
		this.collaborators = collaborators;
	}

	/**
	 * @return the investigators
	 */
	public List<String> getInvestigators() {
		return investigators;
	}

	/**
	 * @param investigators the investigators to set
	 */
	public void setInvestigators(List<String> investigators) {
		this.investigators = investigators;
	}

	/**
	 * @return the otherStudyId
	 */
	public List<String> getOtherStudyId() {
		return otherStudyId;
	}

	/**
	 * @param otherStudyId the otherStudyId to set
	 */
	public void setOtherStudyId(List<String> otherStudyId) {
		this.otherStudyId = otherStudyId;
	}

	/**
	 * @return the sourceId
	 */
	public Long getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	// /**
	//  * @return the siteId
	//  */
	// public String getSiteId() {
	// 	return siteId;
	// }

	// /**
	//  * @param siteId the siteId to set
	//  */
	// public void setSiteId(String siteId) {
	// 	this.siteId = siteId;
	// }

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	private static List<LocationTO> setToLocationList(Set<?> set) {
		List<LocationTO> locationList = new ArrayList<LocationTO>();
		if(!StringUtils.isEmpty(set)) {
			for(Iterator<?> itr = set.iterator(); itr.hasNext(); ) {
				Location location = (Location) itr.next();
				LocationTO locationObject = new LocationTO();
				locationObject.setName(location.getName());
				locationObject.setCity(location.getCity());
				locationObject.setState(location.getState());
				locationObject.setCountry(location.getCountry());
				locationObject.setAddress(location.getRawAddress());
				locationObject.setZip(location.getZip());
				locationList.add(locationObject);
			}
		}
		return locationList;
	}

	private static List<String> setToList(Set<?> set) {
		List<String> strList = new ArrayList<String>();
		if(!StringUtils.isEmpty(set)) {
			for(Iterator<?> itr = set.iterator(); itr.hasNext(); ) {
				strList.add(((BaseEntity) itr.next()).getName());
			}
			// strList  = (List<String>) Arrays.asList(set.toArray(new String [0]));
		}
		return strList;
	}

	/**
	 * Convenience method to transform a List of String to Set of complex object
	 * @param list
	 * @param toClass
	 * @param isContactField
	 * @return
	 */
	private static <T extends TrialField> Set<T> listToSet(List<String> list, Class<?> toClass, boolean isContactField) {
		Set<T> set = new HashSet<T>();
		set = list.stream().map(item -> {
			T field = null;
			try {
				Constructor<?> constructor = toClass.getConstructor(null);
				field = (T) constructor.newInstance(null);
				field.setName(item);
				if(isContactField) {
					((Contact) field).setName(item.split(":::")[0]);
					((Contact) field).setPhone(item.split(":::")[1]);
					((Contact) field).setEmail(item.split(":::")[2]);
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
			return field;
		}).collect(Collectors.toSet());
		/* for(String item : list) {
			TrialField toItem;
			try {
				toItem = (TrialField) toClass.getConstructor(null).newInstance(null);
				toItem.setName(item);
				set.add(toItem);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		} */
		return set;
	}

	/**
	 * Overloaded version to allow skipping last argument
	 * @param list
	 * @param toClass
	 * @return
	 */
	private static <T extends TrialField> Set<T> listToSet(List<String> list, Class<?> toClass) {
		return listToSet(list, toClass, false);
	}

    public String getUrlNctId() {
        return urlNctId;
    }

	public void setUrlNctId(String urlNctId) {
		this.urlNctId = urlNctId;
	}
}
