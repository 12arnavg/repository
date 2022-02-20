package com.getwellsoon.controller;

import java.util.HashMap;
import java.util.Map;

import com.getwellsoon.service.GeoCodingService;
import com.getwellsoon.service.GetWellSoonService;
import com.getwellsoon.service.SitemapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class RootController {

	@Autowired
	private SitemapService sitemapService;

	@Autowired
	private GeoCodingService geoCodingService;
	
	@Autowired
	protected GetWellSoonService getWellSoonService;

	/**
	 * Generates and returns Sitemap XML
	 * @param type
	 * @return
	 */
	@GetMapping(value = "/sitemap/{base}/get/{type}", produces = {MediaType.TEXT_XML_VALUE})
	public String getSitemap(@PathVariable(name = "base") String base, @PathVariable(name = "type") String type) {
		// StringBuilder stringBuilder = new StringBuilder();
		String sitemapXmlIndex = "";
		switch(type.toLowerCase()) {
			case "xml":
				try {
					sitemapXmlIndex = sitemapService.getSitemapIndex(base);
				} catch (Exception e) {
					System.err.println("Exception occurred getting index");
					e.printStackTrace();
				}
		}
		return sitemapXmlIndex; //stringBuilder.toString();
	}

	/**
	 * Write the sitemap in batch of multiple xml and an index file, everything in a single request.
	 * @return
	 */
	@GetMapping(value = "/sitemap/write/{base}")
	public String writeSitemap(@PathVariable(required = false, name = "base") String base) {
		boolean sitemapWritten = sitemapService.generateSitemap(base);
		// SitemapWriterUtil.writeIndex(urls);
		return sitemapWritten ? "sitemap generated" : "failed to generate sitemap!";
	}

	/**
	 * --- IN DEVELOPMENT - DO NOT USE ---
	 * Picks a particular sitemap gZip part and returns it
	 * @param part the gZip file number
	 * @return
	 */
	@GetMapping(value = "/sitemap/{base}/part/{number}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public ResponseEntity<Resource> packSitemap(@PathVariable(name = "base") String base, @PathVariable(name = "number") Integer part){
		byte [] gzBytes = null;
		try{
			gzBytes = sitemapService.getSitemap(base, part);
		}
		catch(Exception e) {
			System.err.println("Exception occurred getting part ".concat(String.valueOf(part)));
		}
		ByteArrayResource byteArrayResource = new ByteArrayResource(gzBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setCacheControl(CacheControl.noCache());
		headers.setPragma("no-cache");
		headers.setExpires(0);
		headers.setContentDisposition(ContentDisposition.parse("attachment; filename=".concat("sitemap").concat(String.valueOf(part).concat(".xml.gz"))));
		// headers.add("Cache-Control", headerValue);
		return ResponseEntity.ok()
						.headers(headers)
						.body(byteArrayResource);
	}

	@GetMapping(value = "/sitemap/{base}/remove", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<Map<String, String>> removeParts(@PathVariable(name = "base") String base){
		Map<String, String> responseMap = new HashMap<String, String>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setCacheControl(CacheControl.noCache());
		headers.setPragma("no-cache");
		headers.setExpires(0);

		try {
			Boolean [] sitemapRemoved = sitemapService.removeSitemap(base);
			responseMap.put("indexRemoved", sitemapRemoved[0] ? "yes" : "no");
			responseMap.put("partsRemoved", sitemapRemoved[1] ? "yes" : "no");
		} catch (Exception e) {
			System.err.println("Exception occurred removing parts");
			e.printStackTrace();
		}
		return ResponseEntity.ok()
			.headers(headers)
			.body(responseMap);
	}

	@PostMapping(value = "/geocode")
	public ResponseEntity<Map<String, Map<String, Double>>> convertToGeoCode( @RequestParam(name = "address") String address) {
		Map<String, Map<String, Double>> response = new HashMap<String, Map<String, Double>>();
		response.put("coordinates", geoCodingService.convertToLatLng(address));
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/report/{type}")		//, produces = {MediaType.TEXT_HTML_VALUE}
	public ResponseEntity<String> sendReortEmail (@PathVariable(name = "type", required = true) String type) {
		if(!type.equals("aggr")) return null;
		return ResponseEntity
				.ok()
				.header("contentType", MediaType.TEXT_HTML_VALUE)
//				.contentType(MediaType.TEXT_HTML)
				.body(getWellSoonService.sendReportAsEmail(type));
	}
}
