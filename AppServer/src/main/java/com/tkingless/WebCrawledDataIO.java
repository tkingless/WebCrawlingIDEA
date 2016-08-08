package com.tkingless;

import com.tkingless.DBobject.MatchEventDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

/**
 * Created by tsangkk on 8/8/16.
 * Thank to Bauke Scholtz, http://balusc.omnifaces.org/2007/07/fileservlet.html
 */
public class WebCrawledDataIO extends HttpServlet {

    final static Logger logger = LogManager.getLogger(WebCrawledDataIO.class);

    //TODO can write a file into the filesharing folder
    //TODO can connect to DB to get data
    //TODO can call CSV, file manager
    //TODO can have working threads, again....my gosh

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    private String filePath;
    private MatchEventDAO workerDAO;

    //ref: http://balusc.omnifaces.org/2007/07/fileservlet.html

    public void init() throws ServletException {

        this.workerDAO = new MatchEventDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia());
        this.filePath = WCDIOconstants.testEnvFileSerlvetABSpath;

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String requestedFile = request.getPathInfo();

        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = new File(filePath, URLDecoder.decode(requestedFile, "UTF-8"));

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = getServletContext().getMimeType(file.getName());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } finally {
            close(output);
            close(input);
        }
    }

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                //TODO Do your thing with the exception. Print it, log it or mail it.
                e.printStackTrace();
            }
        }
    }


}
