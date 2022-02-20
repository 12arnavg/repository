package com.getwellsoon.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.getwellsoon.enumeration.StudyType;
import com.getwellsoon.model.TrialMessage;
import com.getwellsoon.util.DateTimeUtil;
import com.getwellsoon.util.GWSUUIDGenerator;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "trials", uniqueConstraints = {@UniqueConstraint(columnNames = "nct_id", name = "uk-nct_id")})
// @JsonIgnoreProperties(value = {"collaborators", "publications", "conditions", "interventions", "investigators", "responsibleParties", "studySponsors", "contacts", "primaryOutcomes", "secondaryOutcomes"})
public class Trial extends BaseEntityImpl {	//implements EntityOperation 

	// @Column(name = "site_id", columnDefinition = "BINARY(16)", nullable = false)	//, updatable = false
	// private UUID siteId;

	@Column(name="created_at")
	private Date createdAt;

	@Column(name="processed_at")
	private Date processedAt;

	@Column(name="is_deleted")
	private Boolean isDeleted;
	
	@Column(name="first_posted")
	private String firstPosted;
	
	@Column(name = "first_submitted")
	private String firstSubmitted;
	
	@Column(name="last_updated")
	private String lastUpdated;
	
	@Column(name="study_start")
	private String studyStart;
	
	@Column(name="study_completion")
	private String studyCompletion;

	@Lob
	@Column(name="brief_title")
	private String briefTitle;
	
	@Lob
	@Column(name="official_title")
	private String officialTitle;
	
	@Lob
	@Column(name="summary")
	private String summary;
	
	@Lob
	@Column(name="description")
	private String description;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name="study_type")
	private StudyType studyType;
	
	@Column(name="study_phase")
	private String studyPhase;
	
	@Lob
	@Column(name="study_design")
	private String studyDesign;
		
	@Column(name="nct_id")
	private String nctId;

	@Column(name = "enrollment_estimate")
	private String enrollmentEstimate;
	
	@Column(name="enrollment_status")
	private String enrollmentStatus;
	
	@Column(name="enrollment_count")
	private Integer enrollmentCount;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name="min_age")
	private Integer minAge;
	
	@Column(name="max_age")
	private Integer maxAge;

	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Collaborator> collaborators;

	// @OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinTable(
		name = "trial_primaryoutcome",
		joinColumns = @JoinColumn(name = "trial_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "primaryoutcome_id", referencedColumnName = "id")
	)
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<PrimaryOutcome> primaryOutcomes;
	
	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<SecondaryOutcome> secondaryOutcomes;
	
	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Intervention> interventions;

	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Publication> publications;
	
	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Contact> contacts;
	
	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<ResponsibleParty> responsibleParties;
	
	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	// @JsonManagedReference
	private Set<StudySponsor> studySponsors;
	
	@OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Investigator> investigators;

	// @OneToMany(mappedBy = "trial", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	// private Set<OtherStudy> otherStudies;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "source_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_trials_source"))
	private Source source;

	@JoinTable(
		name = "trial_condition",
		joinColumns = {@JoinColumn(name="trial_id", table = "trials", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_trial_condition"))},
		inverseJoinColumns = {@JoinColumn(name="condition_id", table = "conditions", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_condition_trial"))}
	)
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Set<Condition> conditions;

	// @Column(name = "recruitment_status")
	// private String reruitmentStatus;

	@OneToMany(mappedBy = "trial", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<InclusionCriteria> inclusionCriterias;

	@OneToMany(mappedBy = "trial", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ExclusionCriteria> exclusionCriterias;

	@OneToMany(mappedBy = "trial", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Location> locations;

	public Trial() {
		super();
		if(getId() == null) {
			processedAt = DateTimeUtil.getObsoleteMinute();
			// try{
			// 	this.siteId = GWSUUIDGenerator.getGenerated();
			// }
			// catch (Exception e) {
			// 	System.err.println("Exception occurred generating UUID for Trial");
			// }
		}
	}

	public Trial(Source source) {
		this.source = source;
	}
	
	public Trial(String url, Date created_at, Source source) {
		super();
		// this.siteUrl = url;
		this.createdAt = created_at;
		this.source = source;
	}

	public Trial(SiteURL url, Source source) {
		// this.siteUrl = url;
		this.source = source;
	}
	
	// public SiteURL getSiteUrl() {
	// 	return siteUrl;
	// }

	// public void setSiteUrl(SiteURL siteUrl) {
	// 	this.siteUrl = siteUrl;
	// }

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(Date processedAt) {
		this.processedAt = processedAt;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getFirstPosted() {
		return firstPosted;
	}

	public void setFirstPosted(String firstPosted) {
		this.firstPosted = firstPosted;
	}

	public String getFirstSubmitted() {
		return firstSubmitted;
	}

	public void setFirstSubmitted(String firstSubmitted) {
		this.firstSubmitted = firstSubmitted;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getStudyStart() {
		return studyStart;
	}

	public void setStudyStart(String studyStart) {
		this.studyStart = studyStart;
	}

	public String getStudyCompletion() {
		return studyCompletion;
	}

	public void setStudyCompletion(String studyCompletion) {
		this.studyCompletion = studyCompletion;
	}
	
	// public String getReruitmentStatus() {
	// 	return reruitmentStatus;
	// }

	// public void setReruitmentStatus(String reruitmentStatus) {
	// 	this.reruitmentStatus = reruitmentStatus;
	// }

	public Set<PrimaryOutcome> getPrimaryOutcomes() {
		return primaryOutcomes;
	}

	public void setPrimaryOutcomes(Set<PrimaryOutcome> primaryOutcomes) {
		this.primaryOutcomes = primaryOutcomes;
	}

	public Set<SecondaryOutcome> getSecondaryOutcomes() {
		return secondaryOutcomes;
	}

	public void setSecondaryOutcomes(Set<SecondaryOutcome> secondaryOutcomes) {
		this.secondaryOutcomes = secondaryOutcomes;
	}

	public String getBriefTitle() {
		return briefTitle;
	}

	public void setBriefTitle(String briefTitle) {
		this.briefTitle = briefTitle;
	}

	public String getOfficialTitle() {
		return officialTitle;
	}

	public void setOfficialTitle(String officialTitle) {
		this.officialTitle = officialTitle;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String studySummary) {
		this.summary = studySummary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StudyType getStudyType() {
		return studyType;
	}

	public void setStudyType(StudyType studyType) {
		this.studyType = studyType;
	}

	public String getStudyPhase() {
		return studyPhase;
	}

	public void setStudyPhase(String studyPhase) {
		this.studyPhase = studyPhase;
	}

	public String getStudyDesign() {
		return studyDesign;
	}

	public void setStudyDesign(String studyDesign) {
		this.studyDesign = studyDesign;
	}

	public Set<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(Set<Condition> conditions) {
		this.conditions = conditions;
	}

	public Set<Intervention> getInterventions() {
		return interventions;
	}

	public void setInterventions(Set<Intervention> interventions) {
		this.interventions = interventions;
	}

	public Set<Publication> getPublications() {
		return publications;
	}

	public void setPublications(Set<Publication> publications) {
		this.publications = publications;
	}

	public String getEnrollmentEstimate() {
		return enrollmentEstimate;
	}

	public void setEnrollmentEstimate(String enrollmentEstimate) {
		this.enrollmentEstimate = enrollmentEstimate;
	}

	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(String enrollment_status) {
		this.enrollmentStatus = enrollment_status;
	}

	public Integer getEnrollmentCount() {
		return enrollmentCount;
	}

	public void setEnrollmentCount(Integer enrollmentCount) {
		this.enrollmentCount = enrollmentCount;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	public String getNctId() {
		return nctId;
	}

	public void setNctId(String nctId) {
		this.nctId = nctId;
	}

	public Set<ResponsibleParty> getResponsibleParties() {
		return responsibleParties;
	}

	public void setResponsibleParties(Set<ResponsibleParty> responsibleParties) {
		this.responsibleParties = responsibleParties;
	}

	public Set<StudySponsor> getStudySponsors() {
		if(studySponsors == null) studySponsors = new HashSet<StudySponsor>();
		return studySponsors;
	}

	public void setStudySponsors(Set<StudySponsor> studySponsors) {
		this.studySponsors = studySponsors;
	}

	public Set<Collaborator> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(Set<Collaborator> collaborators) {
		this.collaborators = collaborators;
	}
	
	// public Set<OtherStudy> getOtherStudies() {
	// 	return otherStudies;
	// }

	// public void setOtherStudies(Set<OtherStudy> otherStudies) {
	// 	this.otherStudies = otherStudies;
	// }

	public Set<Investigator> getInvestigators() {
		return investigators;
	}

	public void setInvestigators(Set<Investigator> investigators) {
		this.investigators = investigators;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	// public UUID getSiteId() {
	// 	return siteId;
	// }

	// public void setSiteId(UUID siteId) {
	// 	this.siteId = siteId;
	// }

	public void addCollaborator(Collaborator c) {
		if(collaborators == null) collaborators = new HashSet<Collaborator>();
		c.setTrial(this);
		collaborators.add(c);
	}

	public void removeCollaborator(Collaborator c) {
		if(collaborators == null) collaborators = new HashSet<Collaborator>();
		c.setTrial(null);
		collaborators.remove(c);
	}

	// public void addOutcome(Outcome o) {
	// 	if(o instanceof PrimaryOutcome) {
	// 		if(primaryOutcomes == null) primaryOutcomes = new HashSet<PrimaryOutcome>();
	// 		((PrimaryOutcome) o).setTrial(this);
	// 		primaryOutcomes.add((PrimaryOutcome) o);
	// 	}
	// 	else {
	// 		if(secondaryOutcomes == null) secondaryOutcomes = new HashSet<SecondaryOutcome>();
	// 		((SecondaryOutcome) o).setTrial(this);
	// 		secondaryOutcomes.add((SecondaryOutcome) o);
	// 	}
	// }

	// public void removeOutcome(Outcome o) {
	// 	if(o instanceof PrimaryOutcome) {
	// 		if(primaryOutcomes == null) primaryOutcomes = new HashSet<PrimaryOutcome>();
	// 		((PrimaryOutcome) o).setTrial(null);
	// 		primaryOutcomes.remove((PrimaryOutcome) o);
	// 	}
	// 	else {
	// 		if(secondaryOutcomes == null) secondaryOutcomes = new HashSet<SecondaryOutcome>();
	// 		((SecondaryOutcome) o).setTrial(null);
	// 		secondaryOutcomes.remove((SecondaryOutcome) o);
	// 	}
	// }

	public void addPrimaryOutcome(PrimaryOutcome po) {
		if(primaryOutcomes == null) primaryOutcomes = new HashSet<PrimaryOutcome>();
		po.getTrials().add(this);
		primaryOutcomes.add(po);
	}

	public void removePrimaryOutcome(PrimaryOutcome po) {
		if(primaryOutcomes == null) primaryOutcomes = new HashSet<PrimaryOutcome>();
		po.getTrials().add(null);
		primaryOutcomes.remove(po);
	}

	public void addSecondaryOutcome(SecondaryOutcome so) {
		if(secondaryOutcomes == null) secondaryOutcomes = new HashSet<SecondaryOutcome>();
		so.setTrial(this);
		secondaryOutcomes.add(so);
	}

	public void removeSecondaryOutcome(SecondaryOutcome so) {
		if(secondaryOutcomes == null) secondaryOutcomes = new HashSet<SecondaryOutcome>();
		so.setTrial(null);
		secondaryOutcomes.remove(so);
	}

	public void addIntervention(Intervention i) {
		if(interventions == null) interventions = new HashSet<Intervention>();
		i.setTrial(this);
		interventions.add(i);
	}

	public void removeIntervention(Intervention i) {
		if(interventions == null) interventions = new HashSet<Intervention>();
		i.setTrial(null);
		interventions.remove(i);
	}

	public void addPublication(Publication p) {
		if(publications == null) publications = new HashSet<Publication>();
		p.setTrial(this);
		publications.add(p);
	}

	public void removePublication(Publication p) {
		if(publications == null) publications = new HashSet<Publication>();
		p.setTrial(null);
		publications.remove(p);
	}

	public void addContact(Contact c) {
		if(contacts == null) contacts = new HashSet<Contact>();
		c.setTrial(this);
		contacts.add(c);
	}

	public void removeContact(Contact c) {
		if(contacts == null) contacts = new HashSet<Contact>();
		c.setTrial(null);
		contacts.remove(c);
	}

	public void addResponsibleParty(ResponsibleParty rp) {
		if(responsibleParties == null) responsibleParties = new HashSet<ResponsibleParty>();
		rp.setTrial(this);
		responsibleParties.add(rp);
	}

	public void removeResponsibleParty(ResponsibleParty rp) {
		if(responsibleParties == null) responsibleParties = new HashSet<ResponsibleParty>();
		rp.setTrial(null);
		responsibleParties.remove(rp);
	}
	
	public void addStudySponsor(StudySponsor ss) {
		if(studySponsors == null) studySponsors = new HashSet<StudySponsor>();
		ss.setTrial(this);
		studySponsors.add(ss);
	}

	public void removeStudySponsor(StudySponsor ss) {
		if(studySponsors == null) studySponsors = new HashSet<StudySponsor>();
		ss.setTrial(null);
		studySponsors.remove(ss);
	}

	public void addInvestigator(Investigator i) {
		if(investigators == null) investigators = new HashSet<Investigator>();
		i.setTrial(this);
		investigators.add(i);
	}

	public void removeInvestigator(Investigator i) {
		if(investigators == null) investigators = new HashSet<Investigator>();
		i.setTrial(null);
		investigators.remove(i);
	}

	public void addCondition(Condition c) {
		if(conditions == null) conditions = new HashSet<Condition>();
		c.getTrials().add(this);
		conditions.add(c);
	}

	public void removeCondition(Condition c) {
		if(conditions == null) conditions = new HashSet<Condition>();
		c.getTrials().remove(this);
		conditions.remove(c);
	}

	public void addLocation(Location l) {
		if(locations == null) locations = new HashSet<Location>();
		l.setTrial(this);
		locations.add(l);
	}

	public void removeLocation(Location l) {
		if(locations == null) locations = new HashSet<Location>();
		l.setTrial(null);
		locations.remove(l);
	}

	public void addInclusionCriteria(InclusionCriteria ic) {
		if(inclusionCriterias == null) inclusionCriterias = new HashSet<InclusionCriteria>();
		ic.setTrial(this);
		inclusionCriterias.add(ic);
	}

	public void removeInclusionCriteria(InclusionCriteria ic) {
		if(inclusionCriterias == null) inclusionCriterias = new HashSet<InclusionCriteria>();
		ic.setTrial(null);
		inclusionCriterias.remove(ic);
	}

	public void addExclusionCriteria(ExclusionCriteria ec) {
		if(exclusionCriterias == null) exclusionCriterias = new HashSet<ExclusionCriteria>();
		ec.setTrial(this);
		exclusionCriterias.add(ec);
	}

	public void removeExclusionCriteria(ExclusionCriteria ec) {
		if(exclusionCriterias == null) exclusionCriterias = new HashSet<ExclusionCriteria>();
		ec.setTrial(null);
		exclusionCriterias.remove(ec);
	}

	

	// public void addOtherStudy(OtherStudy o) {
	// 	if(otherStudies == null) otherStudies = new HashSet<OtherStudy>();
	// 	o.setTrial(this);
	// 	otherStudies.add(o);
	// }

	// public void removeOtherStudy(OtherStudy o) {
	// 	if(otherStudies == null) otherStudies = new HashSet<OtherStudy>();
	// 	o.setTrial(null);
	// 	otherStudies.remove(o);
	// }

	// @Override
	// public void addToCollection(Set collection, Object t) {
		
	// }
	
	public Set<InclusionCriteria> getInclusionCriterias() {
		return inclusionCriterias;
	}

	public void setInclusionCriterias(Set<InclusionCriteria> inclusionCriterias) {
		this.inclusionCriterias = inclusionCriterias;
	}

	public Set<ExclusionCriteria> getExclusionCriterias() {
		return exclusionCriterias;
	}

	public void setExclusionCriterias(Set<ExclusionCriteria> exclusionCriterias) {
		this.exclusionCriterias = exclusionCriterias;
	}

	/*
	public List<String> getSecondaryIds() {
		return secondary_ids;
	}

	public void setSecondaryIds(List<String> secondary_ids) {
		this.secondary_ids = secondary_ids;
	} */
}