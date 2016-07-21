package com.tkk;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;

@WebServlet(urlPatterns={"/hello"})
public class HelloWorld extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String user = req.getParameter("user");
        req.setAttribute("user", user);
        req.getRequestDispatcher("hello.jsp")
                .forward(req, resp);
    }

    //TODO how appserver connect to WebCrawling

    //TODO make a class of Appeserver can run the codes of WebCrawling when Tomcat startup, and the hooking worker properly shutdown when server down
    public void init() throws ServletException
    {
        System.out.println("----------");
        System.out.println("---------- CrunchifyServletExample Initialized successfully ----------");
        System.out.println("----------");
        //TODO do something meaningful
    }

    //REF: http://crunchify.com/how-to-start-embedded-http-jersey-server-during-java-application-startup/
    //REF: http://crunchify.com/how-to-run-java-program-automatically-on-tomcat-startup/
}

