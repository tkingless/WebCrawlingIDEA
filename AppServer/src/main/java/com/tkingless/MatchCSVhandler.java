package com.tkingless;

import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.FileManager;
import org.bson.Document;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    Document WCDIOdoc,match;

    private boolean lastOutSucceed = false;

    public MatchCSVhandler(Document WCDIOdoc, Document matchDoc, String root, Date now){
        rootPath = root;

        if(matchDoc.containsKey("homeTeam")){
            csvTeamH = matchDoc.getString("homeTeam");;
            csvTeamH = csvTeamH.replace(' ', '_');
        }

        if(matchDoc.containsKey("awayTeam")){
            csvTeamA = matchDoc.getString("awayTeam");
            csvTeamA = csvTeamA.replace(' ', '_');
        }

        csvFile = WCDIOdoc.getInteger("MatchId").toString() + '.' + csvTeamH + '.' + csvTeamA + ".csv";

        subFolder=DateTimeEntity.getDefault_dateFormat().format(now);

        this.WCDIOdoc = WCDIOdoc;
        this.match = matchDoc;

        //make sure root exist
        FileManager.CreateFolder(root);

    }

    public void run() {

        try {
            String absCSVpath = rootPath + "/" + csvFile;
            String archivePath = rootPath + "/";
            if(!WCDIOdoc.containsKey("lastOut")){
                Overwrite();
                lastOutSucceed = true;
                WebCrawledDataIO.logger.debug("no lastOut found, so overwrite");
                return;
            }

            Date lastOutTime = WCDIOdoc.getDate("lastOut");
            String possibleSubfolder = DateTimeEntity.getDefault_dateFormat().format(lastOutTime);
            archivePath += possibleSubfolder+ "/" + csvFile;

            if (!FileManager.CheckFileExist(absCSVpath) ) {

                if(FileManager.CheckFileExist(archivePath)){
                    WebCrawledDataIO.logger.debug("markedEnded csv found in subfolder, so nothing to do");
                    return;
                }

                Overwrite();
                lastOutSucceed = true;
                WebCrawledDataIO.logger.debug("no existing csv found found, so overwrite");
                return;
            }

            Date refTime = null;

            if (WCDIOdoc.containsKey("MarkedEnd")) {
                refTime = WCDIOdoc.getDate("MarkedEnd");

                if(refTime.getTime() <= lastOutTime.getTime()){
                    WebCrawledDataIO.logger.debug("csv out finish, moved csv file");
                    FileManager.RenameFile(absCSVpath,archivePath);
                    return;
                }
            } else {
                refTime = lastOutTime;
            }

            Append(refTime);
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

        List<Document> data = (List<Document>) WCDIOdoc.get("data");
        csvHdr = new FileManager(csvFile);

        try {
            String headers =
                    data.get(0).keySet().stream().map(i -> i.toString()).collect(Collectors.joining(","));
            //add header line
            csvHdr.AppendBufferedOnNewLine(headers);

            for (Document datum : data) {
                StringBuilder lineHead = new StringBuilder();
                for (String key : datum.keySet()) {
                    lineHead.append("\"").append(datum.get(key).toString()).append("\",");
                }
                lineHead.setLength(Math.max(lineHead.length()-1,0));
                csvHdr.AppendBufferedOnNewLine(lineHead.toString());
            }

            csvHdr.Close();
        }catch (Exception e){
            WebCrawledDataIO.logger.error("Overwrite() error",e);
        }

    }

    private void Append(Date refTime){

    }

    @Override
    public String toString(){
        String tmp = null;

        tmp += "csvTeamH: " + csvTeamH;
        tmp += "\ncsvTeamA:" + csvTeamA;

        if(WCDIOdoc != null){
            if(WCDIOdoc.containsKey("MatchId")){
                tmp += "\nMatchId: " + WCDIOdoc.getInteger("MatchId");
            }
            if(WCDIOdoc.containsKey("lastIn")){
                tmp += "\nlastIn: " + WCDIOdoc.getDate("lastIn");
            }
            if(WCDIOdoc.containsKey("MarkedEnd")){
                tmp += "\nMarkedEnd: " + WCDIOdoc.getDate("MarkedEnd");
            }
            if(WCDIOdoc.containsKey("lastOut")){
                tmp += "\nlastOut: " + WCDIOdoc.getDate("lastOut");
            }
        }

        return tmp;
    }

}
