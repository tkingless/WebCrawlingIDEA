package com.tkk.webCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tkk.crawlee.TutorCaseCrawlee;
import com.tkk.utils.logTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class L4TutorCrawler extends tutorCrawler {

	String URL_KEY = "WC_URL";
	String URL_INDEX_KEY = "WC_INDEX_URL";
	static final String threadName = "L4Tutor-thread";

	private static L4TutorCrawler instance = null;

	protected L4TutorCrawler() {
		// exists only to defeat instantiation
		super(tutorCrawler.CrawlerKeyBinding.L4Tutor,threadName);

	}

	public static L4TutorCrawler GetInstance() {

		if (instance == null) {
			instance = new L4TutorCrawler();
		}

		return instance;
	}

	// Start to run function, shall not be called from external
	public void run() {
		try {
			ProcessUrlsAction();
		} catch (Exception e) {
			logTest.logger.error(e);
		}
	}

	protected void ProcessUrlsAction() {

		List<String> onboard_indices = new ArrayList<String>();

		@SuppressWarnings({ "unchecked" })
		Collection<String> idx_urls = (Collection<String>) config.get(URL_INDEX_KEY);

		// suppose should only one matching URL_KEY for those on-board indices
		@SuppressWarnings({ "unchecked" })
		String url = ((List<String>) config.get(URL_KEY)).get(0);

		// load inx board page to get on-board indices
		for (String idx_url : idx_urls) {
			logTest.logger.info("The idx url: " + idx_url);

			try {
				Document idxDoc = Jsoup.connect(idx_url).data("query", "Java").userAgent("Mozilla")
						.cookie("auth", "token").timeout(6000).post();

				Pattern atrbt = Pattern.compile("bk_case_[0-9]{6}");
				Matcher idxMatcher = atrbt.matcher(idxDoc.body().toString());

				while (idxMatcher.find()) {
					String str = idxMatcher.group();
					str = str.substring(str.lastIndexOf('_') + 1);
					onboard_indices.add(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		synchronized (tutorCaseCrawlees) {
			Collections.sort(onboard_indices);

			for (String index : onboard_indices) {
				int idx = Integer.parseInt(index);
				tutorCaseCrawlees.add(new TutorCaseCrawlee(idx, url + index, this));
			}

			logTest.logger.info("[L4Tutor tutorCaseCrawlees] size: " + this.getTutorCaseCrawlees().size());
			tutorCaseCrawlees.notify();
		}
	}

	public void AnalyzeContentAction(TutorCaseCrawlee tutorCaseCrawlee) {
		Document doc = tutorCaseCrawlee.getJdoc();
		HashMap<String, String> searchNodes = new HashMap<String, String>();
		searchNodes.put("Location", "span[class$=title]");
		searchNodes.put("LastUpdateAt", "span[class$=loginTime]");
		searchNodes.put("Details", "div[class$=detail] > div[class$=item]");

		Elements location = doc.select(searchNodes.get("Location"));
		Elements lastUpdate = doc.select(searchNodes.get("LastUpdateAt"));
		Elements eles = doc.select(searchNodes.get("Details"));

		tutorCaseCrawlee.Put("Website","Website: " + this.toString());
		tutorCaseCrawlee.Put("Location", "Location: " + location.text());
		tutorCaseCrawlee.Put("LastUpdateAt", "Last Update: " + lastUpdate.text());
		tutorCaseCrawlee.Put("Time", eles.get(0).text());
		tutorCaseCrawlee.Put("Gender", eles.get(1).text());
		tutorCaseCrawlee.Put("Info", eles.get(2).text());
		tutorCaseCrawlee.Put("Subject", eles.get(3).text());
		tutorCaseCrawlee.Put("Fee", eles.get(4).text());
		tutorCaseCrawlee.Put("Other", eles.get(5).text());
	}

	public void FilterByCritAction() {
		super.FilterByCritAction();
		
		for (Iterator<TutorCaseCrawlee> crawlee_ite = tutorCaseCrawlees.iterator(); crawlee_ite.hasNext();) {
			TutorCaseCrawlee tutorCaseCrawlee = crawlee_ite.next();
			Boolean beDeleted = true;

			if (FilterInBySubject(tutorCaseCrawlee.Context())) {
				if (!FilterByFee(tutorCaseCrawlee)) {
					if (FilterOutByLocation(tutorCaseCrawlee.Context())) {
						beDeleted = false;
					}
				}
			}

			if (beDeleted) {
				logTest.logger.info("[SearchCrit] L4Tutor Going to delete tutorCaseCrawlee: " + tutorCaseCrawlee.getCase_index());
				crawlee_ite.remove();
			}
		}
	}

	public void PostProcessAction() {
		super.PostProcessAction();
		// Result:
		for (TutorCaseCrawlee cr : tutorCaseCrawlees) {
			logTest.logger.info("[SearchCrit] L4Tutor Remaining tutorCaseCrawlee: " + cr.getCase_index());
		}
	}

	@Override
	public String toString() {
		return "L4TutorCrawler";
	}
}
