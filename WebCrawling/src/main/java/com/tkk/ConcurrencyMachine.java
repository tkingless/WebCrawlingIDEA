package com.tkk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.tkk.crawlee.baseCrawlee;
import org.jsoup.nodes.Document;
import com.tkk.utils.Stopwatch;

public class ConcurrencyMachine {

	// TODO: this value is got by testing, different machines should redo.
	static final int maxWorkerNumber = 15;
	private static ConcurrencyMachine instance = null;

	public static ConcurrencyMachine GetInstance() {

		if (instance == null) {
			instance = new ConcurrencyMachine();
		}

		return instance;
	}

	ExecutorService executorService;
	List<Callable<Document>> requests = new ArrayList<Callable<Document>>();

	public ConcurrencyMachine() {
		executorService = Executors.newFixedThreadPool(maxWorkerNumber);
	}

	public synchronized void InvokeQueue() {

		@SuppressWarnings({ "unused" })
		List<Future<Document>> handles = new ArrayList<Future<Document>>();

		Stopwatch timer = new Stopwatch();

		try {
			// By this method, all Future runned and then this parent jump to
			// next line
			if(requests.size() < 60){
				System.out.println("[InvokingRequet] the requests size not qualified, and size is: " + requests.size());
			}
			handles = executorService.invokeAll(requests);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("[Timer] Mass crawling elapsed time: " + timer.GetElapsedTime());

		executorService.shutdownNow();
		requests.clear();

	}

	public synchronized void RegisterQueue(baseCrawlee request) {
			requests.add(request);
	}

	public synchronized void RegisterQueue(List<baseCrawlee> request) {
			requests.addAll(request);
	}
}
