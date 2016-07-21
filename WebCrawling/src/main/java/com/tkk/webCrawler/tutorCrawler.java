package com.tkk.webCrawler;
import java.lang.String;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tkk.utils.*;
import com.tkk.crawlee.TutorCaseCrawlee;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.csv.*;

public abstract class tutorCrawler extends baseCrawler {

	/*
	 * Binding of Crawler object (programming logical concept) with a key Since
	 * there is no reason for a strange web key to tell program run
	 */

	String CONFIG_FILE = "Configs/config.csv";
	protected String CRIT_SUBJECT_KEY = "WC_SEARCH_CRIT";
	protected String CRIT_LOCATION_KEY = "WC_SEARCH_OUT_CRIT";
	protected String CRIT_PRICE_KEY = "WC_SEARCH_COND_PRICE_ABOVE";
	protected String[] config_header_mapping = { "WEB_KEY", "TYPE", "VALUE" };

	protected CrawlingStates mState;
	protected List<TutorCaseCrawlee> tutorCaseCrawlees = new ArrayList<TutorCaseCrawlee>();

	/*
	 * Need it be protected?
	 */
	protected MultiMap<String, String> config = new MultiValueMap<String, String>();
    
	protected tutorCrawler(CrawlerKeyBinding id, String threadName) {
		super(id,threadName);
		ParseInResultAction(mID);
	}
	
	protected void ParseInResultAction(CrawlerKeyBinding id) {
		mState = CrawlingStates.STATE_PARSE_IN_CONFIG;
		
		String Key = id.toString();
		
		//no need to protect csvHdr, as it is just local variable
		try {
			FileManager csvHdr = new CSVmanager(CONFIG_FILE);
			List<CSVRecord> csvRecords = ((CSVmanager) csvHdr).CreateParseInRecord(config_header_mapping);

			for (int i = 1; i < csvRecords.size(); i++) {
				CSVRecord record = csvRecords.get(i);

				String webKey = record.get(config_header_mapping[0]);
				String key = record.get(config_header_mapping[1]);
				String val = record.get(config_header_mapping[2]);

				if ( Key.equals(webKey)){
					//System.out.println("[Apache] apache commons csv here, The WebKey: " + webKey + ", TYPE: " + key
							//+ " and the VALUE: " + val);

					config.put(key, val);
				}
			}
			csvHdr.Close();
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
	
	public List<TutorCaseCrawlee> getTutorCaseCrawlees() {
		return tutorCaseCrawlees;
	}

	protected void ProcessUrlsAction() {
		mState = CrawlingStates.STATE_PROCESS_URL;
	}

	public void AnalyzeContentAction(TutorCaseCrawlee crwl) {
		mState = CrawlingStates.STATE_ANALYSE_CONTENT;
		System.out.println("[tutorCrawler] AnalyzeContentAction() called");
	}

	public void FilterByCritAction() {
		mState = CrawlingStates.STATE_SEARCH_CRIT_FILTER;
	}
	
	protected Boolean FilterByFee(TutorCaseCrawlee tutorCaseCrawlee) {
		int price_above = -1;
		@SuppressWarnings({ "unchecked" })
		Collection<String> price_str = (Collection<String>) config.get(CRIT_PRICE_KEY);
		price_above = Integer.parseInt((String) price_str.toArray()[0]);
		if (price_above != -1) {
			if (tutorCaseCrawlee.GetFee() > price_above)
				return false;
		}
		return true;
	}

	protected Boolean FilterOutByLocation(String crwlVal) {

		@SuppressWarnings({ "unchecked" })
		Collection<String> location_Strs = (Collection<String>) config.get(CRIT_LOCATION_KEY);

		for (String aCrit : location_Strs) {
			Pattern crit = Pattern.compile(aCrit);
			Matcher matcher = crit.matcher(crwlVal);
			if (matcher.find())
				return false;
		}
		return true;
	}

	protected Boolean FilterInBySubject(String crwlVal) {

		@SuppressWarnings({ "unchecked" })
		Collection<String> subject_Strs = (Collection<String>) config.get(CRIT_SUBJECT_KEY);

		for (String aCrit : subject_Strs) {
			Pattern crit = Pattern.compile(aCrit);
			Matcher matcher = crit.matcher(crwlVal);
			if (matcher.find())
				return true;
		}
		return false;
	}

	public void PostProcessAction() {
		mState = CrawlingStates.STATE_POSTPROCESS;
	}

}
