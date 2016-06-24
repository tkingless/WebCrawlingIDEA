package com.tkk.webCrawling;

import com.tkk.webCrawling.webCrawler.*;
import com.tkk.webCrawling.crawleeClass.*;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.*;

public class WebCrawlingMain {

	public static void main(String[] args) throws IOException {

		//Workaround to ensure outputs folders
		FileManager.CreateFolder("Outputs");

		// declared all the websites
		List<baseCrawler> crawlers = new ArrayList<baseCrawler>();
		crawlers.add(HKJCcrawler.GetInstance());

		// WAIT, until constructors finish and have websites get their
		// board indexes
		for (baseCrawler crlr : crawlers) {
			List<TutorCaseCrawlee> crles = null;

			synchronized (crles) {
				try {
					crlr.StartRun();
					crles.wait();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			//System.out.println(String.format("[%s tutorCaseCrawlees] size 2: %s",crlr.toString(),crlr.getTutorCaseCrawlees().size()));
			ConcurrencyMachine.GetInstance().RegisterQueue(null);
		}

		ConcurrencyMachine.GetInstance().InvokeQueue();

		System.out.println("Program main runned to LAST line!");

		}

}
