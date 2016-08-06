package com.tkingless;

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

    /*@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String user = req.getParameter("user");
        
        req.setAttribute("user", user);
        req.getRequestDispatcher("hello.jsp")
                .forward(req, resp);
    }*/

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("---------- HelloWorld servlet Initialized successfully ----------");
        logger.info("This is info, init() called");
    }

    @PreDestroy
    public void OnDestroy() {
        System.out.println("---------- HelloWorld servlet destroyed ----------");
    }

}

