package com.tkingless;

import com.tkingless.utils.CSVmanager;
import com.tkingless.utils.DateTimeEntity;
import org.bson.Document;

/**
 * Created by tsangkk on 8/22/16.
 */
public class MatchCSVhandler {

    String csvFile;
    String csvTeamH;
    String csvTeamA;
    String subFolder;
    String rootPath;
    CSVmanager csVmanager;

    Document WCDIO,match;

    private boolean lastOutSucceed = false;

    //TODO look into WCDIOcsv, look for documents that has not ended writing, defined as !(MarkedEnded&&lastOut>=lastIn), !lastOut:exists

    public MatchCSVhandler(Document WCDIO, Document matchDoc, String root){
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

        subFolder=DateTimeEntity.GetToday();

        this.WCDIO = WCDIO;
        this.match = matchDoc;


    }

    public void run() {

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
