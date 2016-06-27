package com.tkk.webCrawling;

import com.tkk.webCrawling.utils.*;
import com.tkk.webCrawling.webCrawler.*;


import java.io.IOException;
import java.util.*;

public class WebCrawlingMain {

	public static void main(String[] args) throws IOException, InterruptedException {

		//Workaround to ensure outputs folders
		FileManager.CreateFolder("Outputs");

		// declared all the websites
		List<baseCrawler> crawlers = new ArrayList<baseCrawler>();
		crawlers.add(HKJCcrawler.GetInstance());

		/*for (baseCrawler crlr : crawlers) {
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

		ConcurrencyMachine.GetInstance().InvokeQueue();*/

		for (baseCrawler crlr : crawlers) {
				crlr.StartRun();
				//to make current thread join the crlr thread, i.e. wait crlr thread finished to continue.
				crlr.JoinThread();
		}


		System.out.println("Program main runned to LAST line!");

		}

}
