package com.tkingless;

import com.tkingless.utils.CSVmanager;
import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.FileManager;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tsangkk on 8/22/16.
 */
public class MatchCSVhandler {

    String csvFile;
    String csvTeamH;
    String csvTeamA;
    String subFolder;
    String rootPath;
    FileManager csvHdr;

    Document WCDIO,match;

    private boolean lastOutSucceed = false;

    public MatchCSVhandler(Document WCDIO, Document matchDoc, String root, Date now){
        rootPath = root;

        if(matchDoc.containsKey("homeTeam")){
            csvTeamH = matchDoc.getString("homeTeam");;
            csvTeamH = csvTeamH.replace(' ', '_');
        }

        if(matchDoc.containsKey("awayTeam")){
            csvTeamA = matchDoc.getString("awayTeam");
            csvTeamA = csvTeamA.replace(' ', '_');
        }

        csvFile = WCDIO.getInteger("MatchId").toString() + '.' + csvTeamH + '.' + csvTeamA + ".csv";

        subFolder=DateTimeEntity.getDefault_dateFormat().format(now);

        this.WCDIO = WCDIO;
        this.match = matchDoc;

        //make sure root exist
        FileManager.CreateFolder(root);

    }

    public void run() {

        try {
            String absCSVpath = rootPath + "/" + csvFile;
            String archivePath = rootPath + "/";
            if(!WCDIO.containsKey("lastOut")){
                //TODO overwrite, set lastOutSucceed true
                lastOutSucceed = true;
                WebCrawledDataIO.logger.debug("no lastOut found, so overwrite");
                return;
            }

            Date lastOutTime = WCDIO.getDate("lastOut");
            String possibleSubfolder = DateTimeEntity.getDefault_dateFormat().format(lastOutTime);
            archivePath += possibleSubfolder + csvFile;

            if (!FileManager.CheckFileExist(absCSVpath) ) {

                if(FileManager.CheckFileExist(archivePath)){
                    WebCrawledDataIO.logger.debug("markedEnded csv found in subfolder, so nothing to do");
                    return;
                }

                //TODO overwrite, set lastOutSucceed true
                lastOutSucceed = true;
                WebCrawledDataIO.logger.debug("no existing csv found found, so overwrite");
                return;
            }

            Date refTime = null;

            if (WCDIO.containsKey("MarkedEnd")) {
                refTime = WCDIO.getDate("MarkedEnd");

                if(refTime.getTime() <= lastOutTime.getTime()){
                    WebCrawledDataIO.logger.debug("csv out finish, moved csv file");
                    FileManager.RenameFile(absCSVpath,archivePath);
                    return;
                }
            } else {
                refTime = lastOutTime;
            }

            //TODO append csv file and set lastOutSucceed true
            lastOutSucceed = true;

        } catch (Exception e){
            WebCrawledDataIO.logger.error("handler run error",e);
        }

    }

    public boolean isLastOutSucceed() {
        return lastOutSucceed;
    }

    public Document getMatchDoc() {
        return match;
    }

    private void Overwrite(){

    }

    private void Append(){

    }

    private void lastWrite(){
        //TODO, create subfolder, put written csv to the folder
    }

    @Override
    public String toString(){
        String tmp = null;

        tmp += "csvTeamH: " + csvTeamH;
        tmp += "\ncsvTeamA:" + csvTeamA;

        if(WCDIO != null){
            if(WCDIO.containsKey("MatchId")){
                tmp += "\nMatchId: " + WCDIO.getInteger("MatchId");
            }
            if(WCDIO.containsKey("lastIn")){
                tmp += "\nlastIn: " + WCDIO.getDate("lastIn");
            }
            if(WCDIO.containsKey("MarkedEnd")){
                tmp += "\nMarkedEnd: " + WCDIO.getDate("MarkedEnd");
            }
            if(WCDIO.containsKey("lastOut")){
                tmp += "\nlastOut: " + WCDIO.getDate("lastOut");
            }
        }

        return tmp;
    }

}
