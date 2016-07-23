package com.tkk;

import java.io.*;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns = {"/hello"})
public class HelloWorld extends HttpServlet {

    final static Logger logger = LogManager.getLogger(HelloWorld.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String user = req.getParameter("user");
        req.setAttribute("user", user);
        req.getRequestDispatcher("hello.jsp")
                .forward(req, resp);
    }

    //TODO make a class of Appeserver can run the codes of WebCrawling when Tomcat startup, and the hooking worker properly shutdown when server down
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("---------- HelloWorld servlet Initialized successfully ----------");
        //TODO do something meaningful

        //TODO get System.out to a log file

        logger.info("This is info, init() called");
    }

    @PreDestroy
    public void OnDestroy() {
        System.out.println("---------- HelloWorld servlet destroyed ----------");

        /*
          log.info("Signaling worker to stop current job/not begin another.");
  this.worker.stop();
  while(this.worker.isProcessingJob()) {
    Thread.sleep(3 * 1000);
  }
  log.info("Worker successfully stopped");
  log.info("destroy() complete");
         */
    }

    //TODO consider ServletContextListner instead

    //REF: http://crunchify.com/how-to-run-java-program-automatically-on-tomcat-startup/
}

