package com.tkingless;

import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.FileManager;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tsangkk on 8/22/16.
 */
public class MatchCSVhandler {

    String csvFile;
    String csvTeamH="";
    String csvTeamA="";
    String league="";
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

        if(matchDoc.containsKey("league")){
            league = matchDoc.getString("league");
            league = league.replace(' ', '_');
        }

        csvFile = league + "." + WCDIOdoc.getInteger("MatchId").toString() + '.' + csvTeamH + '.' + csvTeamA + ".csv";

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

            if (WCDIOdoc.containsKey("MarkedEnd")) {
                Date endTime = WCDIOdoc.getDate("MarkedEnd");

                if(endTime.getTime() <= lastOutTime.getTime()){
                    WebCrawledDataIO.logger.debug("csv out finish, moved csv file");
                    FileManager.CreateFolder(rootPath+"/"+possibleSubfolder);
                    FileManager.RenameFile(absCSVpath,archivePath);
                    return;
                }
            }

            Append(lastOutTime);
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
        csvHdr = new FileManager(rootPath+"/"+csvFile);

        try {

            WebCrawledDataIO.logger.trace("Overwrite(), id: "+ data.get(0).getInteger("MatchId"));

            List<String> headers = Arrays.asList("Recorded Time","Adjusted Time","Stage","HomeTeamScore","AwayTeamScore","HADhomeOdd","HADdrawOdd","HADawayOdd","HADpoolStatus");

            List<String> pools = (List<String>) match.get("poolTypes");

            String headerStr =
                    data.get(0).keySet().stream().map(i -> i.toString()).collect(Collectors.joining(","));
            //add header line
            csvHdr.AppendBufferedOnNewLine(headerStr);

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

        List<Document> data = (List<Document>) WCDIOdoc.get("data");
        csvHdr = new FileManager(rootPath+"/"+csvFile);

        try {

            WebCrawledDataIO.logger.trace("Append(), id: "+ data.get(0).getInteger("MatchId"));

            for (Document datum : data) {

                Date recorded = datum.getDate("recorded");

                if(recorded.getTime() < refTime.getTime()){
                    continue;
                }

                StringBuilder lineHead = new StringBuilder();
                for (String key : datum.keySet()) {
                    lineHead.append("\"").append(datum.get(key).toString()).append("\",");
                }
                lineHead.setLength(Math.max(lineHead.length() - 1, 0));
                WebCrawledDataIO.logger.trace("Append() a newLine, id: "+ data.get(0).getInteger("MatchId"));
                csvHdr.AppendOnNewLine(lineHead.toString());
            }

            csvHdr.Close();
        }catch (Exception e){
            WebCrawledDataIO.logger.error("Append() error",e);
        }

    }

    @Override
    public String toString(){
        String tmp = "\n";

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
