package com.tkk.DBobject;

import com.mongodb.MongoClient;
import com.tkk.MatchCONSTANTS;
import com.tkk.MongoDBparam;
import com.tkk.WebCrawling.DBobject.MatchEventData;
import com.tkk.utils.DateTimeEntity;
import com.tkk.webCrawler.MatchEventWorker;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tkingless on 7/29/16.
 */

//The Integer is Match id
public class MatchEventDAO extends BasicDAO<MatchEventData, ObjectId> {

    public MatchEventDAO(MongoClient mongoClient, Morphia morphia) {
        super(mongoClient, morphia, MongoDBparam.webCrawlingDB);
    }

    public MatchEventDAO(MongoClient mongoClient, Morphia morphia, String DBname){
        super(mongoClient, morphia, DBname);
    }

    public List<MatchEventData> findAll() {
        return getDatastore().find(MatchEventData.class).asList();
    }

    public List<MatchEventData> findByMatchId (Integer id){
        return getDatastore().find(MatchEventData.class).filter("MatchId = ", id).order("MatchId").asList();
    }

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

