package com.getwellsoon.service;

import com.getwellsoon.repository.SiteUrlRepository;
import com.getwellsoon.sitemap.BatchGenerator;
import com.getwellsoon.util.SitemapUtil;
import com.getwellsoon.util.SitemapUtil.SITEMAP_OF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "sitemapService")
public class SitemapService {
	@Autowired
	protected SiteUrlRepository siteUrlRepository;
	public SitemapService(){
	}

	public boolean generateSitemap(String of) {
		final SITEMAP_OF base = SITEMAP_OF.getValByName(of);
		BatchGenerator batchGenerator = new BatchGenerator(base);
		batchGenerator.setBatchSize(50000);
		batchGenerator.start();
		return true;
	}

	public byte [] getSitemap(String base, Integer part) throws Exception {
		SitemapUtil.INSTANCE.setBase(SITEMAP_OF.getValByName(base));
		return SitemapUtil.INSTANCE.getPart(part);
	}

	public String getSitemapIndex(String base) throws Exception {
		SitemapUtil.INSTANCE.setBase(SITEMAP_OF.getValByName(base));
		return SitemapUtil.INSTANCE.readIndex();
	}

	public Boolean[] removeSitemap(String base) throws Exception {
		SitemapUtil.INSTANCE.setBase(SITEMAP_OF.getValByName(base));
		Boolean[] removed = new Boolean [2];
		removed[0] = SitemapUtil.INSTANCE.removeIndex();
		removed[1] = SitemapUtil.INSTANCE.removeParts();
		return removed;
	}
}