package com.tkk;

import com.tkk.utils.*;
import com.tkk.webCrawler.*;


import java.io.IOException;
import java.util.*;

public class WebCrawlingMain {

	public static void main(String[] args) throws IOException, InterruptedException {

		//Workaround to ensure outputs folders
		FileManager.CreateFolder("Outputs");

		// declared all the websites
		List<baseCrawler> crawlers = new ArrayList<baseCrawler>();
		crawlers.add(HKJCcrawler.GetInstance());

		for (baseCrawler crlr : crawlers) {
				crlr.NewThreadRun();
				//to make current thread join the crlr thread, i.e. wait crlr thread finished to continue.
				crlr.JoinThread();
		}

		System.out.println("Program main runned to LAST line!");

		}

}
