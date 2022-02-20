package com.getwellsoon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.getwellsoon.entity.Source;
import com.getwellsoon.service.GetWellSoonService;

@RestController
@RequestMapping("/sources")
public class SourceController {

	@Autowired
	private GetWellSoonService getWellSoonService;
	
	//aggregation

	@RequestMapping(method = RequestMethod.GET, value = "/get")
	public List<Source> getAllSources() {
		return getWellSoonService.getAllSources();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get/{id}")
	public Source getSource(@PathVariable int id) {
		return getWellSoonService.getSource(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/add/{name}/{url}")
	public Long addSource(@PathVariable String name, @PathVariable String url) { 
		return getWellSoonService.addSource(name, url);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{id}")
	public void updateSource(@PathVariable int id, @RequestBody Source source) { 
		getWellSoonService.updateSource(id, source);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
	public void deleteSource(@PathVariable int id) { 
		getWellSoonService.deleteSource(id);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete")
	public void deleteAllSources() {
		getWellSoonService.deleteAllSources();
	}
	
}
