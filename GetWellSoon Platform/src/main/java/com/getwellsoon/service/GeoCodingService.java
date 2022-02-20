package com.getwellsoon.service;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageResult;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import com.getwellsoon.model.GeoCode;
import com.getwellsoon.util.AppResourcePropUtil;
import com.getwellsoon.util.InitializedBeanUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@Service
public class GeoCodingService {
	private AppResourcePropUtil propUtil;
	private JOpenCageGeocoder openCageGeoCoder;
	/*public class RequestBuilder extends Thread {
		@Override
		public synchronized void start() {
			super.start();
			
		}

		public String build(String address) {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append("https://geocode.search.hereapi.com/v1/geocode");
			urlBuilder.append("?q=");
			urlBuilder.append(address);
			urlBuilder.append("&apiKey=");
			urlBuilder.append();
			return ;
		}
	}*/
	
	public GeoCodingService() {
		// propUtil = InitializedBeanUtil.getInitializedBean(AppResourcePropUtil.class);
		// --- Using api key supplied by Gaurav Puwar
		openCageGeoCoder = new JOpenCageGeocoder("5ee893eb090c48f296cd92cbbb113cfa");//propUtil.getOpenCageApiKey());
	}

	public Map<String, Double> convertToLatLng(String address) {
		Map<String, Double> latLngResult = new HashMap<String, Double>();
		
		/*StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("https://geocode.search.hereapi.com/v1/geocode");
		urlBuilder.append("?q=");
		urlBuilder.append(address);
		urlBuilder.append("&apiKey=");
		urlBuilder.append("lULCE2-x_scXHnAQTsYU6OXSGO7WBoy8eHMugvcAOao");
		WebClient hereWebClient = WebClient.create(urlBuilder.toString());
		ResponseSpec response = hereWebClient.get()
				.accept(MediaType.APPLICATION_JSON)
				.retrieve();
		
		String jsonString = response.bodyToMono(String.class).block();
		Map<String, Object> jsonMap = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>(){}.getType());
		jsonMap.keySet().forEach(System.out::println);
		
		if(jsonMap.containsKey("items")) {
			List<?> itemsList = (List<?>) jsonMap.get("items");
			if(itemsList != null && itemsList.size() > 0) {
				Map<?, ?> items = (Map<?, ?>) itemsList.get(0);
				if(items.containsKey("position")) {
					latLngResult = (Map<String, Double>) items.get("position");
				}
			}
		}*/
		
		JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);
		JOpenCageResponse convResponse = openCageGeoCoder.forward(request);
		if(convResponse == null) return null;
		List<JOpenCageResult> results = convResponse.getResults();
		if(results.size() == 0) return null;

		latLngResult.put("lat", results.get(0).getGeometry().getLat());
		latLngResult.put("lng", results.get(0).getGeometry().getLng());
		return latLngResult;
	}

	public String convertToAddress(GeoCode code) {
		JOpenCageReverseRequest request = new JOpenCageReverseRequest(code.getLat(), code.getLng());
		JOpenCageResponse convResponse = openCageGeoCoder.reverse(request);
		if(convResponse == null) return null;

		List<JOpenCageResult> results = convResponse.getResults();
		if(results.size() == 0) return null;

		String address = results.get(0).getFormatted();
		return address;
	}
}

/*class HereAPI {
	public HereAPI() {}
	final static String URL = "https://geocode.search.hereapi.com/v1/geocode";
	final static String API_KEY = "lULCE2-x_scXHnAQTsYU6OXSGO7WBoy8eHMugvcAOao";
//	String address;
//	public String getAddress() {
//		return address;
//	}
//	public void setAddress(String address) {
//		this.address = address;
//	}
}*/
