package com.getwellsoon.sitemap;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.getwellsoon.model.SitemapDO;
import com.getwellsoon.repository.ConditionRepository;
import com.getwellsoon.repository.TrialRepository;
import com.getwellsoon.util.AppResourcePropUtil;
import com.getwellsoon.util.InitializedBeanUtil;
import com.getwellsoon.util.SitemapUtil;
import com.getwellsoon.util.SitemapUtil.SITEMAP_OF;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Batch generator for sitemaps
 */
public class BatchGenerator extends Thread {

	protected TrialRepository trialRepository;

	protected ConditionRepository conditionRepository;

	private AppResourcePropUtil propUtil;

	private int pageNumber;

	private int batchSize;

	private List<String> generatedFiles;
	
	private SitemapUtil.SITEMAP_OF base;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public BatchGenerator(SITEMAP_OF base){
		this.base = base;
		SitemapUtil.INSTANCE.setBase(base);
		propUtil = InitializedBeanUtil.getInitializedBean(AppResourcePropUtil.class);
		trialRepository = InitializedBeanUtil.getInitializedBean(TrialRepository.class);
		conditionRepository = InitializedBeanUtil.getInitializedBean(ConditionRepository.class);
	}

	@Override
	public synchronized void start() {
		if(batchSize < 1000) batchSize = 1000;

		Pageable pageable = PageRequest.of(0, batchSize);
		generatedFiles = new ArrayList<String>();
		SitemapBatch batch;
		
		switch(base) {
			case TRIALS:
			while (true) {
				Page<Object[]> page = trialRepository.findIdAndNctIdAndProcessedAtAndEnrollmentStatus(pageable);
				List<Object[]> resultList = page.getContent();
				
				SitemapDO [] sitemapDOArray = new SitemapDO [resultList.size()];
				ListIterator<Object[]> listItr = resultList.listIterator();

				Object[] trialObj;
				while(listItr.hasNext()) {
					trialObj = listItr.next();
					sitemapDOArray[listItr.nextIndex() - 1] = new SitemapDO(String.valueOf(trialObj[0]),
																				String.valueOf(trialObj[1]),
																				String.valueOf(trialObj[2]),
																				String.valueOf(trialObj[3]));
				}

				batch = new SitemapBatch(sitemapDOArray, page.getNumber() + 1);
				try {
					synchronized(batch) {
						generatedFiles.add(batch.run());
					}
				}
				catch (Exception e) {
					System.err.println("Exception occurred while executing batch.");
					e.printStackTrace();
				}
				
				if(!page.hasNext()) break;
				pageable = page.nextPageable();
			}
			break;
			
			case CONDITIONS:
				Page<String> page = conditionRepository.findSlug(pageable);
				List<String> resultList = page.getContent();

				SitemapDO [] sitemapDOArray = new SitemapDO [resultList.size()];
				ListIterator<String> listItr = resultList.listIterator();

				String slugValue;
				while(listItr.hasNext()) {
					slugValue = listItr.next();
					sitemapDOArray[listItr.nextIndex() - 1] = new SitemapDO(slugValue);
				}

				batch = new SitemapBatch(sitemapDOArray, page.getNumber() + 1);
				try {
					synchronized(batch) {
						generatedFiles.add(batch.run());
					}
				}
				catch (Exception e) {
					System.err.println("Exception occurred while executing batch.");
					e.printStackTrace();
				}
				
				if(!page.hasNext()) break;
				pageable = page.nextPageable();
		}

		if(generatedFiles != null && generatedFiles.size() > 0) {
			try {
				SitemapUtil.INSTANCE.writeIndex(generatedFiles);
			} catch (Exception e) {
				System.err.println("Exception occurred when genrating sitemap index file");
				e.printStackTrace();
			}
		}
	}
}

/**
 * Batch to generate individual sitemap file and later packing it into a GZip (.gz)
 */
class SitemapBatch {//implements Runnable 
	private SitemapDO [] urlArray;
	private int pageNumber;
	public SitemapBatch(SitemapDO [] urlList, int pageNumber) {
		this.pageNumber = pageNumber;
		urlArray = urlList.clone();
	}

	// @Override
	/**
	 * Run the batch to generate a sitemap file with the supplied urls
	 * @return true if the file was successfully written; If one or more exceptions occures it means
	 * the process did not finish generating the sitemap file
	 */
	public String run() {
		String finishedFileName = "";
		try {
			String xmlFileName = SitemapUtil.INSTANCE.writeSitemap(urlArray, pageNumber);
			finishedFileName = SitemapUtil.INSTANCE.packSingleSitemap(xmlFileName, true);
			// if(!StringUtils.isEmpty(finishedFileName)) this.notify();
		} catch (Exception e) {
			System.err.println("Writing operation incomplete");
			e.printStackTrace();
		}

		return finishedFileName;
	}
}