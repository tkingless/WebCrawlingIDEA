package com.tkk;

import com.tkk.WebCrawling.DBobject.MatchEventData;
import com.tkk.utils.DateTimeEntity;
import com.tkk.webCrawler.MatchEventWorker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tkingless on 7/30/16.
 */
public class CrawlingDBobjectConverter {

    public static void ExplainEventWorkerToDBdata (MatchEventWorker worker, MatchEventData data){
        data.setMatchId(Integer.parseInt(worker.getMatchId()));
        data.setMatchKey(worker.getMatchKey());

        String[] teams = worker.getMatchTeams().split("vs");
        data.setHomeTeam(teams[0]);
        data.setAwayTeam(teams[1]);

        List<java.lang.String> pools = new ArrayList<>();
        for (MatchCONSTANTS.InplayPoolType type: worker.getMatchPools()){
            pools.add(type.toString());
        }
        data.setPoolTypes(pools);

        if(worker.getCommenceTime() != null){
            data.setCommence(worker.getCommenceTime().GetTheInstant());
        }

        DateTimeEntity now = new DateTimeEntity();
        data.setCreatedAt(now.GetTheInstant());
        data.setLastModifiedAt(now.GetTheInstant());

    }
}

/*
   private ObjectId id;
    private Integer MatchId;

    private String MatchKey;
    private HashMap<Date,String> stageUpdates;
    private HashMap<Date,String> scoreUpdate;
    private HashMap<Date,Integer> cornerTotUpdate;

    private String homeTeam;
    private String awayTeam;
    private List<String> poolTypes;

    private Date commence;
    private Date actualCommence;
    private Date endTime;
    private Date createdAt;
    private Date lastModifiedAt;
 */
