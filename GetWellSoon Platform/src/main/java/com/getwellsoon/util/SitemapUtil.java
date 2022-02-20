package com.getwellsoon.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.Exception;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.getwellsoon.enumeration.XMLTags;
import com.getwellsoon.model.SitemapDO;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SitemapUtil {
	public static enum SITEMAP_OF {
		CONDITIONS("conditions"),
		TRIALS("trials");
		
		private final String base;
		private SITEMAP_OF(String base) {
			this.base = base;
		}

		private static Map<String, SITEMAP_OF> statusBaseMap;
		static {
			statusBaseMap = new HashMap<String, SITEMAP_OF>();
			for (SITEMAP_OF status : values()) {
				statusBaseMap.put(status.base, status);
			}
		}
		
		public static SITEMAP_OF getValByName(String base) {
			return statusBaseMap.get(base);
		}

		public String getVal() {
			return base;
		}
	}
	
	private String directory;

	private static final String SITEMAP_INDEX_FILENAME = "sitemap-index.xml";

	private static final String SITEMAP_FILENAME = "sitemap.xml";

	private static final String SITEMAP_GZIP_FILENAME = "sitemap.xml.gz";
	
	private static final float SITEMAP_MAX_PRIORITY = 0.8f;
	
	private static final float SITEMAP_MID_PRIORITY = 0.5f;
	
	private static enum CHANGE_FREQUENCY {
		CHANGE_FREQUENCY_WEEKLY("weekly");
		
		private final String value;
		private CHANGE_FREQUENCY(String value) {
			this.value = value;
		}
		
		public String getVal() {
			return value;
		}
	}

	private SITEMAP_OF base;
	public void setBase(SITEMAP_OF base) {
		SitemapUtil.INSTANCE.base = base;
		File dir = new File(base.getVal());
		if(! new File(base.getVal()).isDirectory()) {
			dir.mkdir();
		}
		directory = dir.getAbsolutePath();
	}

	private SimpleDateFormat dateFormat;

	private AppResourcePropUtil propUtil;
	
	private String todayDate;

	public static final SitemapUtil INSTANCE = new SitemapUtil();

	private SitemapUtil () {
		propUtil = InitializedBeanUtil.getInitializedBean(AppResourcePropUtil.class);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		todayDate = dateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * Write individual sitemap XML file which should be later packed into a GZip archive.
	 * @param sitemapDOArray
	 */
	public synchronized String writeSitemap(SitemapDO [] sitemapDOArray, int part) throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		FileWriter writer = null;

		String fileName = directory + File.separatorChar + SITEMAP_FILENAME.split("\\.")[0]
						.concat(String.valueOf(part))
						.concat(".")
						.concat(SITEMAP_FILENAME.split("\\.")[1]);
		File myFile = new File(fileName);

		try {
			if(!myFile.exists()) {
				// System.err.println("No file with name sitemap exists!");
				myFile.createNewFile();
				writer = new FileWriter(myFile);
				writer.write(XMLTags.XML_START.Value() + XMLTags.SITEMAP_ROOT_START.Value());
			}
			else writer = new FileWriter(myFile);

			if(StringUtils.isEmpty(sitemapDOArray)) {
				throw new InvalidParameterException("Incorrect way to write sitemap: No URLs supplied!");
			}
			try {
				StringBuilder tagBuilder = new StringBuilder();
				for(SitemapDO sitemapDO : sitemapDOArray) {
					tagBuilder.append( XMLTags.URL_TAG_START.Value() );
					tagBuilder.append( XMLTags.LOC_TAG_START.Value() );
					tagBuilder.append( propUtil.getServerEndpoint() );
					tagBuilder.append( base == SITEMAP_OF.CONDITIONS ? propUtil.getConditionMapping().concat("/") : propUtil.getTrialMapping().concat("/") );
					tagBuilder.append( sitemapDO.getPathParamValue().concat("/"));
					tagBuilder.append( XMLTags.LOC_TAG_END.Value() );
					tagBuilder.append( XMLTags.LASTMOD_TAG_START.Value() );
					tagBuilder.append( base == SITEMAP_OF.TRIALS? 
							dateFormat.format( dateFormat.parse(sitemapDO.getProcessedAtDate()))
							: todayDate);
					tagBuilder.append( XMLTags.LASTMOD_TAG_END.Value() );
					
					// For Trials if trial is active then we are adding priority tag with max value else mid value
					// For condition we are keeping priority as Mid value
					tagBuilder.append( XMLTags.PRIORITY_TAG_START.Value() );
					tagBuilder.append(base != SITEMAP_OF.TRIALS ?
							SITEMAP_MID_PRIORITY
							: TrialHelper.activeTrialStatus().contains(sitemapDO.getEnrollmentStatus()) ?
									SITEMAP_MAX_PRIORITY
									: SITEMAP_MID_PRIORITY);
					tagBuilder.append( XMLTags.PRIORITY_TAG_END.Value() );
					
					// Adding Change Frequency tag for Conditions as weekly
					if(base == SITEMAP_OF.CONDITIONS) {
						tagBuilder.append( XMLTags.CHANGE_FREQUENCY_TAG_START.Value() );
						tagBuilder.append(CHANGE_FREQUENCY.CHANGE_FREQUENCY_WEEKLY.getVal());
						tagBuilder.append( XMLTags.CHANGE_FREQUENCY_TAG_END.Value() );
					}
					tagBuilder.append( XMLTags.URL_TAG_END.Value() );
				}
				tagBuilder.append( XMLTags.SITEMAP_ROOT_END.Value() );
				writer.append( tagBuilder.toString() );
				tagBuilder.setLength(0);
			}
			catch (Exception e) {
				System.err.println("Sitemap writer exception...");
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			System.err.println("sitemap file access exception...");
			e.printStackTrace();
		}
		finally {
			try { if(writer != null) writer.close(); }
			catch (Exception e) { e.printStackTrace(); }
		}

		// packSingleSitemap(fileName, true);
		return fileName.replace(directory + File.separatorChar, "");
	}

	/**
	 * Write out the sitemap index file which will be submitted to Google.
	 * @param fileNameList an Array of URLs stored within the SiteURL table
	 */
	public void writeIndex(List<String> fileNameList) throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		FileWriter writer = null;
		try {
			File sitemapIndexFile = new File(directory + File.separatorChar + SITEMAP_INDEX_FILENAME);
			if(!sitemapIndexFile.exists()) {
				// System.err.println("No file with name sitemap exists!");
				sitemapIndexFile.createNewFile();
				writer = new FileWriter(sitemapIndexFile);
				writer.write(XMLTags.XML_START.Value() + XMLTags.SITEMAP_INDEX_ROOT_START.Value());
			}
			else writer = new FileWriter(sitemapIndexFile);

			if(StringUtils.isEmpty(fileNameList)) {
				throw new IllegalAccessException("Incorrect way to write sitemap index: No URLs supplied!");
			}
			try {
				StringBuilder tagBuilder = new StringBuilder();
				String todayDate = dateFormat.format(Calendar.getInstance().getTime());

				for(String fileName : fileNameList) {
					tagBuilder.append( XMLTags.SITEMAP_INDEX_TAG_START.Value() );
					tagBuilder.append( XMLTags.LOC_TAG_START.Value() );
					tagBuilder.append( propUtil.getServerEndpoint() );
					tagBuilder.append( base == SITEMAP_OF.CONDITIONS ? propUtil.getConditionMapping().concat("/") : propUtil.getTrialMapping().concat("/"));
					tagBuilder.append( fileName );
					tagBuilder.append( XMLTags.LOC_TAG_END.Value() );
					tagBuilder.append( XMLTags.LASTMOD_TAG_START.Value() );
					tagBuilder.append( todayDate );
					tagBuilder.append( XMLTags.LASTMOD_TAG_END.Value() );
					tagBuilder.append( XMLTags.SITEMAP_INDEX_TAG_END.Value() );
				}
				tagBuilder.append( XMLTags.SITEMAP_INDEX_ROOT_END.Value() );
				writer.append( tagBuilder.toString() );
				tagBuilder.setLength(0);
			}
			catch (Exception e) {
				System.err.println("Sitemap index writer exception...");
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			System.err.println("sitemap file access exception...");
			e.printStackTrace();
		}
		finally {
			try { if(writer != null) writer.close(); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}

	/**
	 * Compress each of the sitemap xml part into a GZip...
	 */
	public synchronized String packSingleSitemap(String fileName, Boolean cleanAfterPacking) throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		String gzipFileName = directory + File.separatorChar + fileName.concat(".gz");
		BufferedInputStream bufferedInputStream = null;
		GZIPOutputStream gzipOutputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(directory + File.separatorChar + fileName));
			byte xmlBytes [];
			if(bufferedInputStream.available() > 0) {
				xmlBytes = new byte [bufferedInputStream.available()];
				bufferedInputStream.read(xmlBytes);
				gzipOutputStream = new GZIPOutputStream(new FileOutputStream(gzipFileName));
				gzipOutputStream.write(xmlBytes);

				if(cleanAfterPacking) {
					new File(directory + File.separatorChar + fileName).delete();
				}
			}
		}
		catch (IOException e) {
			System.err.println("GZip packing exception..");
			e.printStackTrace();
		}
		finally {
			try {bufferedInputStream.close();}
			catch (Exception e) {e.printStackTrace();}
			try {
				if(gzipOutputStream != null) {
					gzipOutputStream.finish();
					gzipOutputStream.close();
				}
			}
			catch (Exception e) {e.printStackTrace();}
		}

		return gzipFileName.replace(directory + File.separatorChar, "");
	}

	/**
	 * Remove the sitemap index file
	 * @return <code>true</code> if the file was deleted successfully, <code>false</code> othewise
	 */
	public boolean removeIndex() throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		File indexFile = new File( directory + File.separatorChar + SITEMAP_INDEX_FILENAME );

		if( indexFile.exists() ) {
			return indexFile.delete();
		}
		return false;
	}

	/**
	 * Remove the sitemap parts including any extracted / left over xml counter parts from it.
	 * @return <code>true</code> if removed, <code>false</code> otherwise
	 * @throws Exception
	 */
	public boolean removeParts() throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		Path thisDir = FileSystems.getDefault().getPath(directory + File.separatorChar);
		String dashes = String.format("%080d%n", 0).toString().replace("0", "-");
		System.out.format("Current app dir: [%s]%n%s%n", thisDir.toAbsolutePath().toString(), dashes);
		try (DirectoryStream<Path> folderStream = Files.newDirectoryStream(thisDir)) {
			Iterator<Path> pathItr = folderStream.iterator();
			while(pathItr.hasNext()) {
				Path nextItem = pathItr.next();
				boolean isPart = nextItem.getFileName().toString().matches("sitemap\\d{1,3}.xml(.gz)?");
				System.out.format("Asset: <%S>%nLocation on drive: [%s]%n", nextItem.getFileName().toString(), nextItem.toAbsolutePath().toString());
				if(isPart) {
					System.out.format("Removing %S ......%n", nextItem.toUri().toString());
					// boolean isFolder = nextItem.toFile().isDirectory();
					nextItem.toFile().delete();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while checking path of this directory!");
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Decompress to extract the sitemap;
	 * This is only for testing the decompression, not meant to be used in production...
	 */
	public void unpackSingleSitemap() throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		FileOutputStream outputStream = null;
		GZIPInputStream gzipInputStream = null;
		try {
			byte [] xmlBytes = new byte [1024];
			gzipInputStream = new GZIPInputStream(new FileInputStream(SITEMAP_GZIP_FILENAME));
			gzipInputStream.read(xmlBytes);
			gzipInputStream.close();

			outputStream = new FileOutputStream(directory + File.separatorChar + "sitemap-new.xml");
			outputStream.write(xmlBytes);
		}
		catch (Exception e) {
			System.err.println("GZip unpacking exception..");
			e.printStackTrace();
		}
		finally {
			try { if(outputStream != null) outputStream.close();}
			catch (Exception e) { e.printStackTrace(); }
		}
	}

	/**
	 * Read the sitemap index and return as a String
	 * @return Sitemap XML in form of a String
	 */
	public String readIndex() throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		String indexContent = "";
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(directory + File.separatorChar + SITEMAP_INDEX_FILENAME));
			byte [] readBytes = null;
			if(bis.available() > 0) {
				readBytes = new byte [bis.available()];
				bis.read(readBytes);
				if(readBytes.length > 0) {
					indexContent = new String(readBytes, StandardCharsets.UTF_8);
					System.out.format("Sitemap index contents..:%s%n%n", indexContent);
				}
			}
		}
		catch(IOException readingException) {
			System.err.println("Exception occurred reading Sitemap Index.........");
			readingException.printStackTrace();
		}
		finally {
			try { if(bis != null) bis.close(); }
			catch (IOException closingException) { closingException.printStackTrace(); }
		}

		return indexContent;
	}

	/**
	 * Read individual sitemap XML gZip file that was previously created; return null byte [] of not available.
	 * @param part The number of the gZip file
	 * @return gZip sitemap file
	 */
	public byte[] getPart(Integer part) throws Exception {
		if(!(INSTANCE instanceof SitemapUtil) || base == null) throw new Exception("SitemapUtil not initialized; Instantiate and set base for which category the sitemap should be created.");

		File zipFile = new File(directory + File.separatorChar + "sitemap".concat(String.valueOf(part)).concat(".xml.gz"));
		byte [] readBytes = null;
		if(!zipFile.exists()) return readBytes;

		BufferedInputStream bInputStream = null;
		try {
			bInputStream = new BufferedInputStream(new FileInputStream(zipFile));
			if(bInputStream.available() > 0) {
				readBytes = new byte [bInputStream.available()];
				bInputStream.read(readBytes);
			}
		}
		catch (Exception e) {
			System.err.println("Exception occurred reading gZip part!");
			e.printStackTrace();
		}
		finally {
			try {
				if(bInputStream != null) bInputStream.close();
			} catch (Exception e) {
				System.err.println("Problem closing gZip reading stream!");
				e.printStackTrace();
			}
		}
		return readBytes;
	}
}
