package com.getwellsoon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.getwellsoon.entity.BaseEntity;
import com.getwellsoon.entity.StudySponsor;
import com.getwellsoon.entity.Trial;
import com.getwellsoon.model.FilterTrialTO;
import com.getwellsoon.model.TrialMessage;
import com.getwellsoon.service.GetWellSoonService;
// import com.getwellsoon.service.ITextParsingService;
// import com.getwellsoon.service.TextParsingService;

import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin
@RestController
@RequestMapping("/trials")
@ComponentScan(basePackages = {"com.service"})
public class TrialController {

	@Autowired
	private GetWellSoonService getWellSoonService;

	// @Autowired
	// private TextParsingService textParsingService;

	@PostMapping(value = "/typecheck")
	public Trial getFromList(@RequestBody TrialMessage trialInfo) {
		return getWellSoonService.setFromList(trialInfo);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get")
	public List<TrialMessage> getAllTrials() {
		return getWellSoonService.getAllTrials();
	}

	/**
	 * Get Trial by id (Primary key)
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/get/{id}")
	public TrialMessage getTrialById(@PathVariable long id) {
		return getWellSoonService.getTrialById(id);
	}

	/**
	 * Get Trial by the NCTID
	 * @param nctId
	 * @return
	 */
	@GetMapping(value = "/get/nctid/{nctid}")
	public TrialMessage getTrialByNctId(@PathVariable(name = "nctid") String nctId) {
		return getWellSoonService.getTrialById(nctId);
	}

	/**
	 * Add trial with given source
	 * @param source_id source ID
	 * @param urls batch of URLs to add as site URLs
	 * @return IDs of saved URLs
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add/{source_id}")
//	public List<Long> addTrials(@PathVariable long source_id, @RequestBody List<String> urls) { 
//		return getWellSoonService.addTrials(source_id, urls);
//	}
	public Map<String, List> addTrials(@PathVariable long source_id, @RequestBody List<String> urls) { 
		return getWellSoonService.addTrials(source_id, urls);
	}
	
	/**
	 * Update the trial with aggregation data
	 * @param id trial ID
	 * @param trial_info trial aggregation data
	 * @return status of the update operation
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update/id/{id}")
	public ResponseEntity<Map<String, String>> updateTrial(
			@RequestParam(name = "withcoord", required = false, defaultValue = "0") short withcoord,
			@PathVariable long id,
			@RequestBody TrialMessage trial_info) { 
		Long savedTrialId = getWellSoonService.updateTrial(id, trial_info, withcoord);
		Map<String, String> status = new HashMap<String, String>();
		status.put("saved", String.valueOf(savedTrialId));
		System.out.println("Data received:" + trial_info);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setCacheControl(CacheControl.noCache());
		headers.setPragma("no-cache");
		headers.setExpires(0);

		return ResponseEntity
			.ok()
			.headers(headers)
			.body(status);
	}

	/**
	 * Delete a trial by ID
	 * @param id trial ID
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/id/{id}")
	public void deleteTrial(@PathVariable long id) {
		getWellSoonService.deleteTrial(id);
	}

	/**
	 * Delete all trials without check
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete")
	public void deleteAllTrials() {
		getWellSoonService.deleteAllTrials();
	}
	

	/**
	 * Get batch of trials limiting to a given batch size
	 * @param batchSize limit for getting trial URLs
	 * @param active flag if set to 1 only active trials will be sent, if set to 0 or is missing non-active trials will be sent
	 * @param sourceId source ID for the URLs
	 * @return list of objects with source id and url
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/urls")
	public List<TrialMessage> getTrialsBatch(
			@RequestParam(name = "active", required = false, defaultValue = "-1") short active,
			@RequestParam (name = "size", defaultValue = "10") Integer batchSize,
			@RequestParam (name = "src", required = false) Long sourceId) {
		return getWellSoonService.getTrialsBatch(batchSize, sourceId, active);

	}

	@PostMapping(value = "/search/age")
	public List<TrialMessage> getTrialsByMinMaxAge (
						@RequestParam(name = "min", required = false, defaultValue = "20") Integer min,
						@RequestParam(name = "max", required = false, defaultValue = "80") Integer max,
						@RequestParam(name = "batchSize", required = false, defaultValue = "100") Integer batchSize) throws Exception {

		if(min >= max) {
			throw new Exception("Min age should be less than max age");
		}

		List<TrialMessage> trialList = getWellSoonService.searchByMinMaxAge(min, max, batchSize);
		return trialList;
	}

	@PostMapping(value = "/search/gender")
	public List<TrialMessage> getTrialsByGender (
						@RequestParam(name = "gender", required = false, defaultValue = "male") String gender,
						@RequestParam(name = "batchSize", required = false, defaultValue = "100") Integer batchSize) throws Exception {

		List<TrialMessage> trialList = getWellSoonService.searchByGender(gender, batchSize);
		return trialList;
	}

	/**
	 * Combined filter for multiple trial fields
	 * @param filterTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/filter")	//, consumes = {MediaType.APPLICATION_JSON_VALUE}
	public Map<String, Object> getFilterTrials ( @RequestBody FilterTrialTO filterTO ) throws Exception {
		if( ObjectUtils.isEmpty(filterTO.getPageNumber()) || filterTO.getPageNumber() <= 1 )
			filterTO.setPageNumber(0);
		if( ObjectUtils.isEmpty(filterTO.getBatchSize()) || filterTO.getBatchSize() <= 0 )
			filterTO.setBatchSize(100);

		Map<String, Object> trialList = getWellSoonService.filterTrials(filterTO);
		return trialList;
	}

	/**
	 * Trial data transfer from active production DB to potential production DB
	 * @param sourceId
	 * @param trialInfo
	 * @return
	 */
	@PostMapping(value="/migrate/{sourceId}")
	public ResponseEntity<String> migrateTrialData(@PathVariable(name = "sourceId") Long sourceId,
															@RequestBody TrialMessage trialInfo) {
		Long savedTrialId = getWellSoonService.addTrialRow(sourceId, trialInfo);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.setCacheControl(CacheControl.noCache());
		headers.setPragma("no-cache");
		headers.setExpires(0);

		String message = String.format("Trial row migrated with: %d", savedTrialId);
		return ResponseEntity.ok()
						.headers(headers)
						.body(message);
	}

	// @PostMapping(value="/criteria")
	// public void testCriteria(@RequestBody TrialMessage criteriaData) {
	// 	textParsingService.parseText("", criteriaData.getInclusionCriterias(), criteriaData.getExclusionCriterias());
	// }
}
