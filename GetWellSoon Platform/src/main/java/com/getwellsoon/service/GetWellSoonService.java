package com.getwellsoon.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;

import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.getwellsoon.entity.Collaborator;
import com.getwellsoon.entity.Condition;
import com.getwellsoon.entity.Condition_;
import com.getwellsoon.entity.Contact;
import com.getwellsoon.entity.ExclusionCriteria;
import com.getwellsoon.entity.InclusionCriteria;
import com.getwellsoon.entity.Intervention;
import com.getwellsoon.entity.Investigator;
import com.getwellsoon.entity.Location;
import com.getwellsoon.entity.Location_;
import com.getwellsoon.entity.PrimaryOutcome;
import com.getwellsoon.entity.PrimaryOutcome_;
import com.getwellsoon.entity.Publication;
import com.getwellsoon.entity.ResponsibleParty;
import com.getwellsoon.entity.SecondaryOutcome;
import com.getwellsoon.entity.SiteURL;
import com.getwellsoon.entity.Source;
import com.getwellsoon.entity.StudySponsor;
import com.getwellsoon.entity.Trial;
import com.getwellsoon.entity.Trial_;
import com.getwellsoon.model.FilterTrialTO;
import com.getwellsoon.model.LocationTO;
import com.getwellsoon.model.TrialMessage;
import com.getwellsoon.model.TrialUpdateTO;
import com.getwellsoon.repository.ConditionRepository;
import com.getwellsoon.repository.SiteUrlRepository;
import com.getwellsoon.repository.SourceRepository;
import com.getwellsoon.repository.TrialRepository;
import com.getwellsoon.util.GWSUUIDGenerator;
import com.getwellsoon.util.GeomUtils;
import com.getwellsoon.util.ListSetUtils;
import com.getwellsoon.util.MathUtils;
import com.getwellsoon.util.TrialHelper;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

@Service
public class GetWellSoonService {

	@Autowired
	private SourceRepository sourceRepository;

	@Autowired
	private SiteUrlRepository siteUrlRepository;

	@Autowired
	private TrialRepository trialRepository;

	@Autowired
	private ConditionRepository conditionRepository;

	@Autowired
	private GeoCodingService geoCodingService;

	@PersistenceContext
	private EntityManager eManager;

	protected CriteriaBuilder criteriaBuilder;

	// public void savePoint() {
	// 	Point aPoint = new WKTReader().read("POINT (3.44 4.10)").
	// }

	public List<Source> getAllSources() {
		return (List<Source>) sourceRepository.findAll();
	}

	public Source getSource(long id) {
		return sourceRepository.findById(id).get();
	}

	public long addSource(String name, String url) {
		Source source = new Source(name, url);
		sourceRepository.save(source);
		return source.getId();
	}
	
	public void updateSource(long sourceId, Source source) {
		sourceRepository.save(source);
	}

	public void deleteSource(long sourceId) {
		sourceRepository.deleteById(sourceId);
	}

	public void deleteAllSources() {
		sourceRepository.deleteAll();
	}

	public List<TrialMessage> getAllTrials() {
		List<Trial> trialList = trialRepository.findAll();
		List<TrialMessage> trialMessageList = new ArrayList<TrialMessage>();
		ListIterator<Trial> trialItr = trialList.listIterator();
		while(trialItr.hasNext()) {
			trialMessageList.add( TrialMessage.copy(trialItr.next()) );
		}
		return trialMessageList;
	}

	/**
	 * Get Trial by id (PK)
	 * @param trialId
	 * @return
	 */
	@Transactional(readOnly = true)
	public TrialMessage getTrialById(long trialId) {
		List<Trial> trials = trialRepository.findByIdWithCollections(trialId); // findById(trialId).get();
		if(trials.size() > 0)
		return TrialMessage.copy(trials.get(0));
		return new TrialMessage();
	}

	@Transactional
	public Map<String, List> addTrials(long source_id, List<String> urls) {
		urls = urls.stream().filter(item ->
			item != null
			&& ( item instanceof String
				&& ( !item.trim().equalsIgnoreCase("null")
				&& !item.trim().equals("") ) )
		).collect(Collectors.toList());
		
		List<Long> insertedTrialIds = new ArrayList<Long>();
		List<String> notInserted = new ArrayList<String>();
		
		urls.stream().forEach(url ->{
			SiteURL matchedSiteUrl = siteUrlRepository.findOneByUrl(url);
			if(matchedSiteUrl != null) {
				notInserted.add(url);
			}
			else {
				Trial trial = new Trial();
//				trial.setSource(sourceRepository.getOne(source_id));
//				trialRepository.save(trial);
//	
//				SiteURL siteURL = new SiteURL();
//				siteURL.setUrl(url);
//				siteURL.setTrial(trial);
//				siteURL.setCreatedDate(new Date());
//	
//				Long siteUrlId = siteUrlRepository.save(siteURL).getId();
//				System.out.printf("Site URL saved %d", siteUrlId);
				insertedTrialIds.add(trial.getId());
			}
		});
		

//		String [] matchedUrls = siteUrlRepository.getUrlsByUrls(urls.toArray(new String [0])).toArray(new String [0]);
//
//		String [] nonPersistedUrls = new String [Math.max(urls.size(), matchedUrls.length) - Math.min(urls.size(), matchedUrls.length)];
//		nonPersistedUrls = urls.stream().filter(url ->
//			!Arrays.asList(matchedUrls).stream().anyMatch(
//					url::equalsIgnoreCase))
//				.toArray(String[]::new);

//		int count = 0;
//		nonPersistedUrls = (String[]) arr.toArray(new String [0]);
//		for(String url : urls) {
//			if(!Arrays.asList(matchedUrls).stream().anyMatch(url::equalsIgnoreCase))
//				nonPersistedUrls [count++] = url;
//		}

		//--- BEGIN: Turned off original logic to add URLs till duplication logic is verified..
		// String[] strUrls = new String [0];
		// strUrls = urls.toArray(strUrls);
		
//		for(String url : nonPersistedUrls) {
//			Trial trial = new Trial();
//			trial.setSource(sourceRepository.getOne(source_id));
//			trialRepository.save(trial);
//
//			SiteURL siteURL = new SiteURL();
//			siteURL.setUrl(url);
//			siteURL.setTrial(trial);
//			siteURL.setCreatedDate(new Date());
//
//			Long siteUrlId = siteUrlRepository.save(siteURL).getId();
//			System.out.printf("Site URL saved %d", siteUrlId);
//			trial_ids.add(trial.getId());
//		}

		//--- FINISH:
		Map<String, List> urlInsertReportMap = new HashMap<String, List> ();
		urlInsertReportMap.put("inserted", insertedTrialIds);
		urlInsertReportMap.put("notInserted", notInserted);
		return urlInsertReportMap;
	}
	
	@Transactional
	public Long updateTrial(long trialId, TrialMessage trialModel, short withcoord) {
		Trial trial = null;
		Long savedId = null;
		// -- If url based nctid and aggregated nctid matches we can safely update data
		if(trialModel.getNctId().equalsIgnoreCase(trialModel.getUrlNctId())) {
			trial = trialRepository.findById(trialId).get();

			savedId = updateTrialNow(trial, trialModel, withcoord);
		}
		// -- In the case nctid has difference amongst part of url and aggregation
		// -- we have to update with the reference nctid
		else {
			// -- First get trial by nctid
			List<Trial> trials = trialRepository.findOneByNctId(trialModel.getNctId());
			trial = trials.size() > 0 ? trials.get(0) : null;

			// -- If trial could not be found by nctid, find it by id
			if(trial == null) {
				trial = trialRepository.findById(trialId).get();
			}

			// -- Get SiteURL by nctid on ref_nctid field
			List<SiteURL> siteURLs = siteUrlRepository.findByRefNctId(trialModel.getNctId());

			if(siteURLs == null || siteURLs.size() == 0) {
				// -- Persist the nctid as ref_nctid in SiteURL
				// -- It is safe to update the trial data once the ref_nctid is saved
				siteURLs = siteUrlRepository.findByUrlEndingWith(trialModel.getUrlNctId());
				SiteURL siteUrl = siteURLs.size() > 0 ? siteURLs.get(0) : null;
				if(siteUrl == null) return null;
				siteUrl.setRefNctId(trialModel.getNctId());
				siteUrlRepository.save(siteUrl);
			}
			// else {
				// -- This is consecutive update taking place on a Trial
				// -- Reference nctid and the stored nctid in trial matches
				// -- It is safe to update the trial data.
				// SiteURL siteUrl = siteURLs.get(0);
				// else {
				// 	siteUrlRepository.delete(siteUrl);
				// }
			// }

			savedId = updateTrialNow(trial, trialModel, withcoord);
		}
		return savedId;
	}

	protected Long updateTrialNow(Trial trial, TrialMessage trialModel, short withcoord) {
		//--- Populate everything from the model to entity
		trial = TrialMessage.preparedEntity(trial, trialModel);
		// trial.setNctId(trialModel.getNctId());
		
		if(!StringUtils.isEmpty(trialModel.getPrimaryOutcomes())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getPrimaryOutcomes());

			for (String strName : trialModel.getPrimaryOutcomes()) {
				// query.select(criteriaBuilder.count(root.get(PrimaryOutcome_.name))).where(criteriaBuilder.equal(root.get(PrimaryOutcome_.name), strName));
				// List<Long> count = eManager.createQuery(query).getResultList();
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				PrimaryOutcome outcome = new PrimaryOutcome();
				outcome.setName(strName);
				trial.addPrimaryOutcome(outcome);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getSecondaryOutcomes())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getSecondaryOutcomes());

			for (String strName : trialModel.getSecondaryOutcomes()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				SecondaryOutcome outcome = new SecondaryOutcome();
				outcome.setName(strName);
				trial.addSecondaryOutcome(outcome);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getStudySponsor())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getStudySponsors());

			for (String strName: trialModel.getStudySponsor()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				StudySponsor sponsor = new StudySponsor();
				sponsor.setName(strName);
				trial.addStudySponsor(sponsor);
			}
		}
		
		if(!StringUtils.isEmpty(trialModel.getInterventions())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getInterventions());

			for (String strName : trialModel.getInterventions()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				Intervention intervention = new Intervention();
				intervention.setName(strName);
				trial.addIntervention(intervention);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getPublications())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getPublications());

			for (String strName : trialModel.getPublications()) {
				if( StringUtils.isEmpty(strName) || StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				Publication publication = new Publication();
				publication.setName(strName);
				trial.addPublication(publication);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getContacts())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getContacts());

			for (String strName : trialModel.getContacts()) {
				if( StringUtils.isEmpty(strName) ) continue;
				if(strName.contains("|")) {
					String [] parts = strName.split("[|]");
					Contact contact = null;
					if(parts.length > 0) {
						if( savedNames.contains(parts[0].trim()) ) {
							contact = trial.getContacts().stream().collect(Collectors.toList()).get(savedNames.indexOf(parts[0].trim()));// filter(c -> c.getName() == parts[0].trim()).collect(Collectors.toList()).get(0);
						}
						else {
							contact = new Contact();
							contact.setName(parts[0].trim());
						}

						if(parts.length > 1) contact.setPhone(parts[1].trim());
						if(parts.length > 2) contact.setEmail(parts[2].trim());
					}
					trial.addContact(contact);
				}
			}
		}

		if(!StringUtils.isEmpty(trialModel.getResponsibleParty())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getResponsibleParties());

			for (String strName : trialModel.getResponsibleParty()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				ResponsibleParty party = new ResponsibleParty();
				party.setName(strName);
				trial.addResponsibleParty(party);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getCollaborators())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getCollaborators());

			for (String strName : trialModel.getCollaborators()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				Collaborator collab = new Collaborator();
				collab.setName(strName);
				trial.addCollaborator(collab);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getInvestigators())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getInvestigators());

			for (String strName : trialModel.getInvestigators()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				Investigator investigator = new Investigator();
				investigator.setName(strName);
				trial.addInvestigator(investigator);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getInclusionCriterias())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getInclusionCriterias());

			for (String strName : trialModel.getInclusionCriterias()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				InclusionCriteria ic = new InclusionCriteria();
				ic.setName(strName);
				trial.addInclusionCriteria(ic);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getExclusionCriterias())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getExclusionCriterias());

			for (String strName : trialModel.getExclusionCriterias()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				ExclusionCriteria ec = new ExclusionCriteria();
				ec.setName(strName);
				trial.addExclusionCriteria(ec);
			}
		}

		if(!StringUtils.isEmpty(trialModel.getLocations()) && withcoord == 1) {
			Map<String, Geometry> savedLocations = ListSetUtils.setToCoordinatesMap(trial.getLocations());

			for (LocationTO locationObject : trialModel.getLocations()) {
				final boolean coordsSaved = savedLocations.get(locationObject.getName()) != null;
				if( StringUtils.isEmpty(locationObject)
				|| StringUtils.isEmpty(locationObject.getName())
				|| ( savedLocations.containsKey(locationObject.getName()) && coordsSaved )) continue;

				Location location = null;
				if( !coordsSaved ) {
					location = trial.getLocations().stream().filter(item -> item.getName().equals(locationObject.getName())).findFirst().orElse(null);
				}
				
				if(location == null) location = new Location();
				location.setName(locationObject.getName());
				location.setCity(locationObject.getCity());
				location.setState(locationObject.getState());
				location.setCountry(locationObject.getCountry());
				location.setRawAddress(locationObject.getAddress());
				location.setZip(locationObject.getZip());

				Map<String,Double> coords = geoCodingService.convertToLatLng(locationObject.getAddress());
				if(coords != null) {
					Geometry pointGeom = GeomUtils.getPoint(coords);
					location.setCoordinates((Point) pointGeom);
				}

				trial.addLocation(location);
			}
		}

		//--- Populate the conditions separately because it has many to many relation
		// List<Condition> savedConditions = Arrays.asList(trial.getConditions().toArray(new Condition[0]));
		if(!StringUtils.isEmpty(trialModel.getConditions())) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getConditions());

			for(String strName : trialModel.getConditions()) {
				if( StringUtils.isEmpty(strName) || savedNames.contains(strName) ) continue;
				Condition currentCondition = conditionRepository.findOneByName(strName);
				// boolean contained = savedConditions.contains(currentCondition);
				// System.out.format("condition is part of trial---%s", Boolean.toString(contained)); */
				if(currentCondition != null) {
					trial.addCondition(currentCondition);
				}
				else {
					Condition newCondition = new Condition();
					final String slug = strName.replaceAll(Pattern.compile("[^\\w\\t\\s]").toString(), "").replaceAll("[\\t\\s_]+", "-");
					newCondition.setSlug(slug);
					newCondition.setName(strName);
					trial.addCondition(newCondition);
				}
			}
		}

		/* for (String strName : from.getOtherStudyId()) {
			List<String> savedNames = ListSetUtils.setToStringList(trial.getStudySponsors());
			System.out.format("Collection names:: %s", savedNames.toArray());

			OtherStudy study = new OtherStudy();
			study.setName(otherStudy);
			entity.addOtherStudy(study);
		} */

		return trialRepository.save(trial).getId();
	}

	public void deleteTrial(long id) {
		trialRepository.deleteById(id);
	}

	public void deleteAllTrials() {
		trialRepository.deleteAll();
	}

	public List<TrialMessage> getTrialsBatch(Integer batchSize, Long sourceId, short active) {
		Pageable pageable = PageRequest.of(0, batchSize, Sort.by("processedAt").ascending());
		Page<Object[]> trialsPage;
		// List<Trial> trialList = new ArrayList<Trial>();
		if(sourceId != null) {
			trialsPage = active == -1 ?
				trialRepository.findAllUrlsForUpdateBySourceId(sourceId, pageable) :
					active == 1 ?
						trialRepository.findAllUrlsForUpdateBySourceIdAndEnrollmentStatusActive(sourceId, pageable) :
						trialRepository.findAllUrlsForUpdateBySourceIdAndEnrollmentStatusInactive(sourceId, pageable);
		}
		else {
			trialsPage = active == -1 ?
				trialRepository.findAllUrlsForUpdate(pageable) :
					active == 1 ?
						trialRepository.findAllUrlsForUpdateByEnrollmentStatusActive(pageable) :
						trialRepository.findAllUrlsForUpdateByEnrollmentStatusInactive(pageable);
		}
		
		// trialList = trialsPage.stream().collect(Collectors.toList());
		List<TrialMessage> trialMessages = new ArrayList<>();
		
		for (Object[] trial : trialsPage) {
			TrialMessage trialMessage = new TrialMessage();
			trialMessage.setId((Long) trial[0]);

			String siteUrl = siteUrlRepository.findOneByTrialId((Long) trial[0]);
			trialMessage.setUrl(siteUrl);
			trialMessage.setSourceId(((Source) trial[1]).getId());
			trialMessages.add(trialMessage);
		}
		return trialMessages;
	}

	/**
	 * Provision to migrate trials data from active live production DB to potential production DB
	 * which has improved schema structure.
	 * @param sourceId
	 * @param trialInfo
	 * @return
	 */
	@Transactional
	public Long addTrialRow(Long sourceId, TrialMessage trialInfo) {
		Trial trial = TrialMessage.preparedEntity(trialInfo);
		trial.setSource(sourceRepository.getOne(sourceId));
		trialRepository.save(trial);

		SiteURL siteURL = new SiteURL();
		siteURL.setUrl(trialInfo.getUrl());
		siteURL.setTrial(trial);

		Long siteUrlId = siteUrlRepository.save(siteURL).getId();
		System.out.format("Trial info migrated: %d%nSite URL saved: %d", trial.getId(), siteUrlId);
		return trial.getId();
	}

	public Trial setFromList(TrialMessage trialInfo) {
		return TrialMessage.preparedEntity(trialInfo);
	}

	/**
	 * Search Trials by min and max age
	 * @param min the minimum age criteria
	 * @param max the maximum age criteria
	 * @param batchSize the number of records to fetch
	 * @return
	 */
	public List<TrialMessage> searchByMinMaxAge(Integer min, Integer max, Integer batchSize) {
		List<TrialMessage> trials = null;//trialRepository.findByMinAgeAndMaxAge(min, max);
		criteriaBuilder = eManager.getCriteriaBuilder();
		CriteriaQuery<TrialMessage> trialCriteria = criteriaBuilder.createQuery(TrialMessage.class);
		Root<Trial> root = trialCriteria.from(Trial.class);

		trialCriteria.select(criteriaBuilder.construct(TrialMessage.class,
		root.get(Trial_.id),
		// root.get(Trial_.siteId),
		root.get(Trial_.nctId),
		root.get(Trial_.minAge),
		root.get(Trial_.maxAge),
		root.get(Trial_.gender),
		root.get(Trial_.briefTitle),
		root.get(Trial_.summary)))
		.where(
			criteriaBuilder.and(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Trial_.minAge), min),
				criteriaBuilder.lessThanOrEqualTo(root.get(Trial_.maxAge), max)
			));

		trials = eManager.createQuery(trialCriteria).setMaxResults(batchSize).getResultList();
		return trials;
	}

	/**
	 * Search Trials by gender
	 * @param gender the gender criteria
	 * @param batchSize the number of records to fetch
	 * @return
	 */
	public List<TrialMessage> searchByGender(String gender, Integer batchSize) {
		List<TrialMessage> trials = null;

		criteriaBuilder = eManager.getCriteriaBuilder();
		CriteriaQuery<TrialMessage> trialCriteria = criteriaBuilder.createQuery(TrialMessage.class);
		Root<Trial> root = trialCriteria.from(Trial.class);

		trialCriteria.select(criteriaBuilder.construct(TrialMessage.class,
		root.get(Trial_.id),
		// root.get(Trial_.siteId),
		root.get(Trial_.nctId),
		root.get(Trial_.minAge),
		root.get(Trial_.maxAge),
		root.get(Trial_.gender),
		root.get(Trial_.briefTitle),
		root.get(Trial_.summary)))
		.where(
				criteriaBuilder.like(root.get(Trial_.gender), gender.toLowerCase())
			);

		trials = eManager.createQuery(trialCriteria).setMaxResults(batchSize).getResultList();
		return trials;
	}

	/**
	 * Filter the trials by various parameters
	 * @param filterTO
	 * @return
	 */
	public Map<String, Object> filterTrials(FilterTrialTO filterTO) {
		criteriaBuilder = eManager.getCriteriaBuilder();
		CriteriaQuery<TrialMessage> trialCriteria = criteriaBuilder.createQuery(TrialMessage.class);
		Root<Trial> root = trialCriteria.from(Trial.class);
		SetJoin<Trial, Condition> conditionJoin = root.join(Trial_.conditions, JoinType.LEFT);
		SetJoin<Trial, Location> locationJoin = root.joinSet(Trial_.LOCATIONS, JoinType.LEFT);

		Geometry fromPoint = null;
		Expression<Number> distanceExpression = criteriaBuilder.literal(0);
		Expression<Double> sphericFunctionExpression = null;

		Predicate criterian = criteriaBuilder.conjunction();
		criterian = criteriaBuilder.and(criterian, root.get(Trial_.enrollmentStatus).in(TrialHelper.activeTrialStatus()));

		if(filterTO.getGender() != null) {
			criterian = criteriaBuilder.like(root.get(Trial_.gender), filterTO.getGender());
		}

		if(filterTO.getAge() != null && filterTO.getAge() > 0) {
			criterian = criteriaBuilder.and(criterian, criteriaBuilder.and(
				criteriaBuilder.lessThanOrEqualTo(root.get(Trial_.minAge), filterTO.getAge()),
				criteriaBuilder.greaterThanOrEqualTo(root.get(Trial_.maxAge), filterTO.getAge())
			));
		}

		if(filterTO.getName() != null) {
			criterian = criteriaBuilder.and(criterian, criteriaBuilder.like(conditionJoin.get(Condition_.NAME), "%".concat(filterTO.getName()).concat("%")));
		}

		if(filterTO.getSlug() != null) {
			criterian = criteriaBuilder.and(criterian, criteriaBuilder.equal( criteriaBuilder.lower( conditionJoin.get(Condition_.SLUG) ), filterTO.getSlug()));
		}

		if(filterTO.getLocation() != null) {
			Map<String,Double> coords = null;
			final LocationTO locationTO = filterTO.getLocation();
			if(locationTO.getCode() != null) {
				coords = new HashMap<String, Double>();
				coords.put("lat", locationTO.getCode().getLat());
				coords.put("lng", locationTO.getCode().getLng());
				// geoCodingService.convertToAddress(locationTO.getCode());
			}
			else if(locationTO.getLocationText() != null) {
				coords = geoCodingService.convertToLatLng(locationTO.getLocationText());
			}

			if(coords != null) {
				fromPoint = GeomUtils.getPoint(coords);
				System.out.println("Searching FROM:" + fromPoint.toString());
				// coordinateExpression = criteriaBuilder.parameter(Point.class);

				sphericFunctionExpression = criteriaBuilder.function("ST_Distance_Sphere",
					Double.class,
					criteriaBuilder.literal(fromPoint.getCentroid()),
					locationJoin.get(Location_.coordinates));

				distanceExpression = criteriaBuilder.quot(
					sphericFunctionExpression.as(Double.class), criteriaBuilder.literal(MathUtils.MILE_TO_METER_RATIO)
				);
				
				criterian = criteriaBuilder.and(criterian,
					criteriaBuilder.lessThanOrEqualTo(
						distanceExpression.as(Double.class),
						locationTO.getDistance())
				);
			}
		}

		Expression<String> formattedLastUpdated = criteriaBuilder.function(
			"STR_TO_DATE",
			String.class,
			root.get(Trial_.lastUpdated),
			criteriaBuilder.literal( "%M %d,%Y" )
		);

		List<Selection<?>> projectionItems = new ArrayList<Selection<?>>();
		projectionItems.add(root.get(Trial_.id));
		// projectionItems.add(root.get(Trial_.siteId));
		projectionItems.add(root.get(Trial_.nctId));
		projectionItems.add(root.get(Trial_.minAge));
		projectionItems.add(root.get(Trial_.maxAge));
		projectionItems.add(root.get(Trial_.gender));
		projectionItems.add(root.get(Trial_.briefTitle));
		projectionItems.add(root.get(Trial_.summary));
		projectionItems.add(root.get(Trial_.studyStart));
		projectionItems.add(root.get(Trial_.lastUpdated));
		projectionItems.add(distanceExpression.as(Double.class));
		projectionItems.add(locationJoin.get(Location_.rawAddress));
		projectionItems.add(locationJoin.get(Location_.city));
		projectionItems.add(locationJoin.get(Location_.state));
		projectionItems.add(locationJoin.get(Location_.country));
		projectionItems.add(locationJoin.get(Location_.zip));
		projectionItems.add(conditionJoin.get(Condition_.name));

		CompoundSelection<TrialMessage> selection = criteriaBuilder.construct(TrialMessage.class, projectionItems.toArray(new Selection[0]));

		trialCriteria = trialCriteria
			.select(selection)
			.where(criterian)
			.orderBy( criteriaBuilder.desc( formattedLastUpdated ) )
			.groupBy(root.get(Trial_.id));

		TypedQuery<TrialMessage> trialQuery = eManager.createQuery(trialCriteria)
		.setFirstResult(filterTO.getPageNumber().equals(0) ? 0 : (filterTO.getPageNumber() - 1) * filterTO.getBatchSize())
		.setHint(QueryHints.READ_ONLY, true)	// Better performance..
		.setMaxResults(filterTO.getBatchSize());

		List<TrialMessage> trialsList = trialQuery.getResultList();
		
		trialsList.stream().forEach(i -> System.out.println(i.getId()));

		CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
		Root<Trial> countRoot = countCriteria.from(Trial.class);
		locationJoin = countRoot.joinSet(Trial_.LOCATIONS, JoinType.LEFT);
		if(conditionJoin != null) conditionJoin = countRoot.join(Trial_.conditions, JoinType.LEFT);

		countCriteria = countCriteria
		.select(criteriaBuilder.countDistinct(countRoot.get(Trial_.id)))
		.where(criterian)
		.orderBy( criteriaBuilder.desc( formattedLastUpdated ) );
		Long totalResults = eManager.createQuery(countCriteria).getSingleResult();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", trialsList);
		resultMap.put("totalResults", totalResults);
		return resultMap;
	}

	@Transactional(readOnly = true)
	public TrialMessage getTrialById(String nctId) {
		List<Trial> trials = trialRepository.findOneByNctId(nctId);
		return TrialMessage.copy(trials.get(0));
	}

	public String sendReportAsEmail(String type) {
		List<TrialUpdateTO> updateTO_List = trialRepository.getUpdateCount();
		StringBuilder message = new StringBuilder();
		message.append("<table style='min-width:200px;max-width:500px;font-family:Arial'>");
			message.append("<th>");
			message.append("<h3 style='padding-bottom:15px; border-bottom: 1px solid #ddd9d9'>Trials updated by date:</h3>");
			message.append("</th>");
		
		message.append("<tr style='font-weight:800;margin-bottom:5px; border-bottom: 1px solid #d9d9d9;width: 80%'>");
			message.append("<td>");
			message.append("Date");
			message.append("</td>");
			message.append("<td>");
			message.append("Count");
			message.append("</td>");
		message.append("</tr>");
		updateTO_List.forEach(item -> {
			message.append("<tr style='margin-bottom:5px;'>");
				message.append("<td style='font-weight:400'>");
				message.append(item.getProcessedDate());
				message.append("</td>");
				message.append("<td style='color:#1b99cf;text-align:right'>");
				message.append(item.getCount());
				message.append("</td>");
			message.append("</tr>");
		});
		message.append("</table>");
//		return message.toString();

		SendGrid grid = new SendGrid("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");
		Content emailContent = new Content("text/html", message.toString());

		Personalization personalization = new Personalization();
		personalization.addTo(new Email("noreply@getwellsoon.ai"));
		personalization.addCc(new Email("sachin@getwellsoon.ai"));
//		personalization.addCc(new Email("sachin.bhatt@getwellsoon.ai"));
		personalization.addCc(new Email("gaurav@getwellsoon.ai"));
		personalization.setSubject("Trials update report");

		Mail gridMail = new Mail();
		gridMail.addPersonalization(personalization);
		gridMail.setFrom(new Email("sachin.bhatt@getwellsoon.ai"));
		gridMail.addContent(emailContent);

		try {
			Request emailRequest = new Request();
			emailRequest.setMethod(Method.POST);
			emailRequest.setEndpoint("mail/send");
			emailRequest.setBody(gridMail.build());
			Response emailResponse = grid.api(emailRequest);
			return String.valueOf(emailResponse.getStatusCode());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "not-sent";
	}
}
