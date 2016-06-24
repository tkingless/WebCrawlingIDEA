package com.tkk.webCrawling;

import com.tkk.webCrawling.webCrawler.*;
import com.tkk.webCrawling.crawleeClass.TutorCaseCrawlee;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.*;

public class WebCrawlingMain {

	public static void main(String[] args) throws IOException {

		//Workaround to ensure outputs folders
		FileManager.CreateFolder("Outputs");

		// declared all the websites
		List<BaseCrawler> crawlers = new ArrayList<BaseCrawler>();
		crawlers.add(HKJCcrawler.GetInstance());

		// WAIT, until constructors finish and have websites get their
		// board indexes
		for (BaseCrawler crlr : crawlers) {
			List<TutorCaseCrawlee> crles = crlr.getTutorCaseCrawlees();

			synchronized (crles) {
				try {
					crlr.StartRun();
					crles.wait();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			//System.out.println(String.format("[%s tutorCaseCrawlees] size 2: %s",crlr.toString(),crlr.getTutorCaseCrawlees().size()));
			ConcurrencyMachine.GetInstance().RegisterQueue(crles);
		}

		ConcurrencyMachine.GetInstance().InvokeQueue();

		// WAIT, until crawled and tutorCaseCrawlee mature, write to same DB file
		// need to lock the log file
		Date runTime = new Date();

		for (BaseCrawler crlr : crawlers) {
			List<TutorCaseCrawlee> crles = crlr.getTutorCaseCrawlees();
			// @Problem: this is interesting, tutorCaseCrawlees is protected inside Crawler,
			// but I can modifiy it outside here
			for (Iterator<TutorCaseCrawlee> crwl_iter = crles.iterator(); crwl_iter.hasNext(); ) {
				TutorCaseCrawlee crwl = crwl_iter.next();
				try {
					if (Crawlee_DB.GetInstance().LookUpFromDB(crwl, runTime)) {
						crwl_iter.remove();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

			// WAIT, until writing DB file, write result file (this is
			// postprocessing)
			for (BaseCrawler crlr : crawlers) {
				crlr.PostProcessAction();
			}

			System.out.println("Program main runned to LAST line!");

		}

}
