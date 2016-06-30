package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.utils.DateTimeEntity;
import org.jsoup.nodes.Document;

import javax.print.Doc;
import java.util.Timer;

/**
 * Created by tkingless on 6/26/16.
 */

//this is class is to be long-live thread worker to log happening event
public class MatchEventWorker extends baseCrawler {

    enum MatchState {
        STATE_PRE_REGISTERED,
        STATE_MATCH_START,
        STATE_MATCH_LOGGING,
        STATE_MATCH_ENDED
    }

    static final String threadName = "MatchEventWorker-thread";

    String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    //The unique id for this worker
    String matchId;
    long scanPeriod = 5000;

    DateTimeEntity commenceTime;
    MatchState status;
    //the secondary key to be used
    String matchKey;
    String matchTeams;


    public MatchEventWorker(String aMatchId, Document matchKeyDoc, Document statusDoc, Document teamsDoc) {
        super(CrawlerKeyBinding.MatchEvent, threadName+"-"+aMatchId);
        matchId = aMatchId;
        System.out.println("MatchEventWorker constructed, matchId:" + matchId);
        //System.out.println("and allOddsLink: " + linkAddr);
    }

    void ExtractMatcdKey (Document matchKeyDoc) {

    }

    void ExtractStatus(Document statusDoc) {
        //TODO: in some case, suppose the program failed to get the start time of the match event, mark current start time to
        //to the start time
    }

    void ExtractTeams(Document teamsDoc) {

    }

    public boolean IsLive(){
        //TODO
        return false;
    }

    public void LaunchProcess(){
        //TODO, if fullfill criteria, start a monitoring thread
    }

    public void SetupParam(){

    }

    void EmitRequest () {
        //TODO grab data periodically
    }

    //The main loop function
    void Proc(){
    //TODO
    }

    public void Kill(){

    }
}
