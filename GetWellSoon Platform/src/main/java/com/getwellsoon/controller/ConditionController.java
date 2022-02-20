package com.getwellsoon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.getwellsoon.entity.Condition;
import com.getwellsoon.entity.Trial;
import com.getwellsoon.model.ConditionCountTO;
import com.getwellsoon.model.ConditionTO;
import com.getwellsoon.model.FilterTrialTO;
import com.getwellsoon.model.TrialMessage;
import com.getwellsoon.service.ConditionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequestMapping(value = "/conditions")
public class ConditionController {
	public ConditionController() {};

	@Autowired
	private ConditionService conditionService;

	@GetMapping(value = "/get500")
	public List<ConditionCountTO> get500Conditions() {
		return conditionService.getTop500();
	}

	@GetMapping(value = "/get")
	public List<ConditionTO> getAllConditions() {
		return conditionService.getAll();
	}
	
	@PostMapping(value="/get/one")
	public List<TrialMessage> getTrialsForCondition(@RequestBody Condition condition) {
		return conditionService.getTrialByConditionName(condition.getName());
	}

	@GetMapping(value = "/get/matching/{name}")
	public List<String> getConditionsMatching(@PathVariable(value = "name") String name) {
		return conditionService.getListOfConditionsMatching(name);
	}
	
	@GetMapping(value = "/slug/{slug:[\\w\\d-]+}")
	public Map<String, Object> getBySLug(@PathVariable(value = "slug") String slug, HttpServletRequest request, HttpServletResponse response, ModelMap slugModel) {
		List<Condition> conditionList = conditionService.getBySlug(slug);
//		slugModel.addAttribute("name", condition.getName());
		FilterTrialTO filterTO = new FilterTrialTO();
		filterTO.setName(conditionList.get(0).getName());
		slugModel.addAttribute(filterTO);
		
		WebClient trialsClient = WebClient.create(request.getRequestURL().toString().split(request.getServletPath())[0]);
		Mono<FilterTrialTO> monoWrapFilter = Mono.just(filterTO);
		ResponseSpec filterResponse = trialsClient.post()
									.uri("/trials/filter")
									.body(monoWrapFilter, FilterTrialTO.class)
									.retrieve();
		
		Map<String, Object> mapResponse = filterResponse.bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {}).blockFirst();
		mapResponse.put("conditionName", conditionList.get(0).getName());
		return mapResponse;
//		return filterResponse.collectMap(null);
//		responseSpec.bodyToMono(Condition.class).map(conditoin)
//		return new ModelAndView("forward:/trials/filter", slugModel);
	}
	
	@GetMapping(value = "/path")
	public Map<String, String> getContextPath(HttpServletRequest request) {
		Map<String, String> paths = new HashMap<String, String>();
		paths.put("requiredPath", request.getRequestURL().toString().split(request.getServletPath())[0]);
		StringBuilder builder = new StringBuilder();
		builder.append(request.getProtocol());
		builder.append("://");
		builder.append(request.getRemoteAddr());
		builder.append(":");
		builder.append(request.getServerPort());
		paths.put("builtPath", builder.toString());
		return paths;
	}
}
