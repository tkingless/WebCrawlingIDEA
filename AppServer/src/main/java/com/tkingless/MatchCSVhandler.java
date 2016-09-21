package com.tkingless;

import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.FileManager;
import org.bson.Document;

import javax.print.Doc;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tsangkk on 8/22/16.
 */
public class MatchCSVhandler {

    String csvFile;
    String csvTeamH = "";
    String csvTeamA = "";
    String league = "";
    String subFolder;
    String rootPath;
    FileManager csvHdr;

    Document WCDIOdoc, match;
    Boolean hasCHLpool = false;

    private boolean lastOutSucceed = false;

    public MatchCSVhandler(Document WCDIOdoc, Document matchDoc, String root, Date now) {
        rootPath = root;

        if (matchDoc.containsKey("homeTeam")) {
            csvTeamH = matchDoc.getString("homeTeam");
            ;
            csvTeamH = csvTeamH.replace(' ', '_');
        }

        if (matchDoc.containsKey("awayTeam")) {
            csvTeamA = matchDoc.getString("awayTeam");
            csvTeamA = csvTeamA.replace(' ', '_');
        }

        if (matchDoc.containsKey("league")) {
            league = matchDoc.getString("league");
            league = league.replace(' ', '_');
        }

        csvFile = league + "." + WCDIOdoc.getInteger("MatchId").toString() + '.' + csvTeamH + '.' + csvTeamA + ".csv";

        subFolder = DateTimeEntity.getDefault_dateFormat().format(now);

        this.WCDIOdoc = WCDIOdoc;
        this.match = matchDoc;

        //make sure root exist
        FileManager.CreateFolder(root);

        List<String> pools = (List<String>) match.get("poolTypes");
        if (pools.contains("CHL")) {
            hasCHLpool = true;
        }

    }

    public void run() {

        try {
            String absCSVpath = rootPath + "/" + csvFile;
            String archivePath = rootPath + "/";
            if (!WCDIOdoc.containsKey("lastOut")) {
                Overwrite();
                lastOutSucceed = true;
                WebCrawledDataIO.logger.debug("no lastOut found, so overwrite");
                return;
            }

            Date lastOutTime = WCDIOdoc.getDate("lastOut");
            String possibleSubfolder = DateTimeEntity.getDefault_dateFormat().format(lastOutTime);
            archivePath += possibleSubfolder + "/" + csvFile;

            if (!FileManager.CheckFileExist(absCSVpath)) {

                if (FileManager.CheckFileExist(archivePath)) {
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

                if (endTime.getTime() <= lastOutTime.getTime()) {
                    WebCrawledDataIO.logger.debug("csv out finish, moved csv file");
                    FileManager.CreateFolder(rootPath + "/" + possibleSubfolder);
                    FileManager.RenameFile(absCSVpath, archivePath);
                    return;
                }
            }

            Append(lastOutTime);
            lastOutSucceed = true;

        } catch (Exception e) {
            WebCrawledDataIO.logger.error("handler run error", e);
        }

    }

    public boolean isLastOutSucceed() {
        return lastOutSucceed;
    }

    public Document getMatchDoc() {
        return match;
    }

    //This is the ordering of header for csv out
    static List<String> commonHdrs = Arrays.asList("Recorded Time", "Adjusted Time", "Stage", "HomeTeamScore", "AwayTeamScore", "HADhomeOdd", "HADdrawOdd", "HADawayOdd", "HADpoolStatus");
    static List<String> CHLheaders = Arrays.asList("Corner Count", "Corner Line 1", "Corner Line 1 High", "Corner Line 1 Low", "Corner Line 2", "Corner Line 2 High", "Corner Line 2 Low", "CHLpoolStatus");

    private void Overwrite() {

        List<Document> data = (List<Document>) WCDIOdoc.get("data");
        csvHdr = new FileManager(rootPath + "/" + csvFile);

        try {

            WebCrawledDataIO.logger.trace("Overwrite(), id: " + data.get(0).getInteger("MatchId"));

            List<String> headers = new ArrayList<>();
            headers.addAll(commonHdrs);

            if (hasCHLpool) {
                headers.addAll(CHLheaders);
            }

            String headerStr =
                    (new LinkedHashSet<>(headers)).stream().map(i -> i.toString()).collect(Collectors.joining(","));

            //add header line
            csvHdr.AppendBufferedOnNewLine(headerStr);

            Date origin = data.get(0).getDate("recorded");

            for (Document datum : data) {
                csvHdr.AppendBufferedOnNewLine(ConcatenateProperLine(datum, origin));
            }

            csvHdr.Close();
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("Overwrite() error", e);
        }

    }

    private void Append(Date refTime) {

        List<Document> data = (List<Document>) WCDIOdoc.get("data");
        csvHdr = new FileManager(rootPath + "/" + csvFile);

        try {

            WebCrawledDataIO.logger.trace("Append(), id: " + data.get(0).getInteger("MatchId"));

            Date origin = data.get(0).getDate("recorded");

            for (Document datum : data) {

                Date recorded = datum.getDate("recorded");

                if (recorded.getTime() < refTime.getTime()) {
                    continue;
                }

                csvHdr.AppendOnNewLine(ConcatenateProperLine(datum, origin));

                WebCrawledDataIO.logger.trace("Append() a newLine, id: " + data.get(0).getInteger("MatchId"));
            }

            csvHdr.Close();
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("Append() error", e);
        }

    }

    String ConcatenateProperLine(Document data, Date origin) {
        StringBuilder lineHead = new StringBuilder();

        addEntry(lineHead, data.get("recorded").toString());

        //Handling adjust time
        Date now = data.getDate("recorded");
        int elapsed = (int) (now.getTime() - origin.getTime()) / (1000 * 60);
        addEntry(lineHead, elapsed);

        addEntry(lineHead, data.get("stage"));
        addEntry(lineHead, data.get("homeTeamScore"));
        addEntry(lineHead, data.get("awayTeamScore"));

        //Handling HAD odds
        if (data.containsKey("HADhomeOdd")) {
            addEntry(lineHead, data.get("HADhomeOdd"));
        } else {
            addEntry(lineHead, "");
        }

        if (data.containsKey("HADdrawOdd")) {
            addEntry(lineHead, data.get("HADdrawOdd"));
        } else {
            addEntry(lineHead, "");
        }

        if (data.containsKey("HADawayOdd")) {
            addEntry(lineHead, data.get("HADawayOdd"));
        } else {
            addEntry(lineHead, "");
        }

        if (data.containsKey("HADpoolStatus")) {
            addEntry(lineHead, data.get("HADpoolStatus"));
        } else {
            addEntry(lineHead, "");
        }


        if (hasCHLpool) {

            if (data.containsKey("cornerCount")) {
                addEntry(lineHead, data.get("cornerCount"));
            } else {
                addEntry(lineHead, "");
            }

            if (data.containsKey("CHLline_1")) {
                addEntry(lineHead, data.get("CHLline_1"));
            } else {
                addEntry(lineHead, "");
            }

            if (data.containsKey("CHLhigh_1")) {
                addEntry(lineHead, data.get("CHLhigh_1"));
            } else {
                addEntry(lineHead, "");
            }

            if (data.containsKey("CHLlow_1")) {
                addEntry(lineHead, data.get("CHLlow_1"));
            } else {
                addEntry(lineHead, "");
            }

            if (data.containsKey("CHLline_2")) {
                addEntry(lineHead, data.get("CHLline_2"));
            } else {
                addEntry(lineHead, "");
            }

            if (data.containsKey("CHLhigh_2")) {
                Double hightwo = data.getDouble("CHLhigh_2");
                if (!hightwo.equals(0.0)) {
                    addEntry(lineHead, data.get("CHLhigh_2"));
                } else {
                    addEntry(lineHead,"");
                }
            } else {
                addEntry(lineHead, "");
            }

            if (data.containsKey("CHLlow_2")) {
                Double lowtwo = data.getDouble("CHLlow_2");
                if (!lowtwo.equals(0.0)) {
                    addEntry(lineHead, data.get("CHLlow_2"));
                } else {
                    addEntry(lineHead,"");
                }
            } else {
                addEntry(lineHead, "");
            }

            if (data.containsKey("CHLpoolStatus")) {
                addEntry(lineHead, data.get("CHLpoolStatus"));
            } else {
                addEntry(lineHead, "");
            }
        }

        lineHead.setLength(Math.max(lineHead.length() - 1, 0));

        return lineHead.toString();
    }

    void addEntry(StringBuilder str, Object content) {
        try {
            str.append("\"").append(content).append("\",");
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("addEntry error: ", e);
            str.append("\"").append("").append("\",");
        }
    }

    @Override
    public String toString() {
        String tmp = "\n";

        tmp += "csvTeamH: " + csvTeamH;
        tmp += "\ncsvTeamA:" + csvTeamA;

        if (WCDIOdoc != null) {
            if (WCDIOdoc.containsKey("MatchId")) {
                tmp += "\nMatchId: " + WCDIOdoc.getInteger("MatchId");
            }
            if (WCDIOdoc.containsKey("lastIn")) {
                tmp += "\nlastIn: " + WCDIOdoc.getDate("lastIn");
            }
            if (WCDIOdoc.containsKey("MarkedEnd")) {
                tmp += "\nMarkedEnd: " + WCDIOdoc.getDate("MarkedEnd");
            }
            if (WCDIOdoc.containsKey("lastOut")) {
                tmp += "\nlastOut: " + WCDIOdoc.getDate("lastOut");
            }
        }

        return tmp;
    }

}
