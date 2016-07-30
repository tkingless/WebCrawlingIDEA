package com.tkk.crawlee;

import java.io.IOException;
import java.lang.String;
import java.util.HashMap;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.tkk.utils.logTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tkk.webCrawler.*;

public class TutorCaseCrawlee extends baseCrawlee {

	private int case_index;

	public int getCase_index() {
		return case_index;
	}

	private String url;

	public String getUrl() {
		return url;
	}

	private tutorCrawler crawlerBelonged;

	public HashMap<String, String> map = new HashMap<String, String>();

	/*
	 * Current keys of HashMap are:
	 * Location,LastUpdateAt,Time,Gender,Info,Subject,Fee,Other
	 */

	public TutorCaseCrawlee(int idx, String aUrl, tutorCrawler crawler) {
		super(crawler);
		case_index = idx;
		url = aUrl;

	}

	public TutorCaseCrawlee(int idx) {
		case_index = idx;
	}

	public void Put(String Key, String Value) {
		map.put(Key, Value);
	}

	public String Context() {
		String content = "";
		Collection<String> strings = map.values();
		for (String str : strings) {
			content = content + str + System.getProperty("line.separator");
		}
		// logTest.logger.info("[TutorCaseCrawlee] content: " + content);
		return content;
	}

	public int GetFee() {
		if (map.containsKey("Fee")) {
			// logTest.logger.info("[SearchCrit] fee: " + map.get("Fee"));
			Pattern price = Pattern.compile("\\$[0-9]{2,4}");
			Matcher matcher = price.matcher(map.get("Fee"));
			if (matcher.find()) {
				String casePriceStr = matcher.group(0).substring(1);
				int casePrice = 55699;
				casePrice = Integer.parseInt(casePriceStr);
				if (casePrice != 55699)
					return casePrice;
			}
		}
		return 689831;
	}

	public String GetValueByKey(String key) {

		if (map.containsKey(key)) {
			return map.get(key);
		}

		return "";
	}

	public Document call() {
		try {
			
			if(this.getCrawlerBelonged().get_mID() == tutorCrawler.CrawlerKeyBinding.TutorGroup){
				state = State.SUCCESS;
				crawlerBelonged.AnalyzeContentAction(this);
				return null;
			}
			
			Jdoc = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla").cookie("auth", "token").timeout(10000)
					.get();
			String errStr = "Server Error";
			if (Jdoc.title().contains(errStr) || Jdoc.text().contains(errStr)) {
				state = State.FAILURE;
			} else {
				state = State.SUCCESS;
				crawlerBelonged.AnalyzeContentAction(this);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			logTest.logger.error("[Jsoup]by experience, if IO exception, could be timeout");
			state = State.TIME_OUT;
		}
		return Jdoc;
	}
}
