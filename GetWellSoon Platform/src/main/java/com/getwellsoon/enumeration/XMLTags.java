package com.getwellsoon.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Collection of starting and closing XML tags
 */
public enum XMLTags {
	XML_START ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"),
	SITEMAP_INDEX_ROOT_START ("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"),
	SITEMAP_INDEX_ROOT_END ("</sitemapindex>"),
	SITEMAP_INDEX_TAG_START ("\t<sitemap>\n"),
	SITEMAP_INDEX_TAG_END ("\t</sitemap>\n"),
	SITEMAP_ROOT_START ("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"),
	SITEMAP_ROOT_END ("</urlset>"),
	URL_TAG_START ("\t<url>\n"),
	URL_TAG_END ("\t</url>\n"),
	LOC_TAG_START ("\t\t<loc>"),
	LOC_TAG_END ("</loc>\n"),
	LASTMOD_TAG_START ("\t\t<lastmod>"),
	LASTMOD_TAG_END ("</lastmod>\n"),
	PRIORITY_TAG_START ("\t\t<priority>"),
	PRIORITY_TAG_END ("</priority>\n"),
	CHANGE_FREQUENCY_TAG_START ("\t\t<changefreq>"),
	CHANGE_FREQUENCY_TAG_END ("</changefreq>\n");

	private final String value;

	private static Map<XMLTags, String> tagValueMap;

	public String Value() {
		return value;
	}

	XMLTags(String value) {
		this.value = value;
	}

	static {
		tagValueMap = new HashMap<XMLTags, String>();
		for (XMLTags tag: values()) {
			tagValueMap.put(tag, tag.value);
		}
	}
}