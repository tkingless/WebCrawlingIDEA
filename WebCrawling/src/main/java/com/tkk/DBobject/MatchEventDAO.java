package com.tkk.DBobject;

import com.mongodb.MongoClient;
import com.tkk.MongoDBparam;
import com.tkk.WebCrawling.DBobject.MatchEventData;
import com.tkk.utils.DateTimeEntity;
import com.tkk.webCrawler.MatchEventWorker;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public MatchEventData findByMatchId (Integer id){
        return findOne("MatchId",id);
    }

    public boolean IsMatchRegisteredBefore (Integer id){
        return exists("MatchId",id);
    }

    public void RegisterMatchEventWorker (MatchEventWorker worker){
        Integer id = Integer.parseInt(worker.getMatchId());
        if(!IsMatchRegisteredBefore(id)){
            SaveMatchEventWorker(worker);
        }else{
            MatchEventData DBdata = findByMatchId(id);
            MatchEventData grabbedData = new MatchEventData();
            FutureEventWorkerToDBdata(worker,grabbedData);

            if(!DBdata.equals(grabbedData)){
                UpdateDBdata(grabbedData,worker.getWorkerTime().GetTheInstant());

            }
        }
    }

    public void MarkAMatchEndTime (MatchEventWorker worker){
        MatchEventData data = findByMatchId(Integer.parseInt(worker.getMatchId()));

    }

    public void InsertField (MatchEventWorker worker, String field, Object val){
        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(Integer.parseInt(worker.getMatchId()));
        UpdateOperations<MatchEventData> ops = getDatastore().createUpdateOperations(MatchEventData.class).set(field,val);
        getDatastore().update(query,ops);
    }

    public boolean QueryDataFieldExists (MatchEventWorker worker, String field){
        boolean existing = false;

        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(Integer.parseInt(worker.getMatchId())).field(field).exists();
        MatchEventData data = query.get();
        if(data != null){
            existing = true;
        }
        return existing;
    }

    void SaveMatchEventWorker(MatchEventWorker worker){
        MatchEventData data = new MatchEventData();
        FutureEventWorkerToDBdata(worker,data);
        this.save(data);

    }

    void FutureEventWorkerToDBdata(MatchEventWorker worker, MatchEventData data){
        data.setMatchId(Integer.parseInt(worker.getMatchId()));
        data.setMatchKey(worker.getMatchKey());

        String[] teams = worker.getMatchTeams().split("vs");
        data.setHomeTeam(teams[0]);
        data.setAwayTeam(teams[1]);

        //TODO usable
        /*List<java.lang.String> pools = new ArrayList<>();
        for (MatchCONSTANTS.InplayPoolType type: worker.getMatchPools()){
            pools.add(type.toString());
        }
        data.setPoolTypes(pools);*/

        if(worker.getCommenceTime() != null){
            data.setCommence(worker.getCommenceTime().GetTheInstant());
        }

        DateTimeEntity now = new DateTimeEntity();
        data.setCreatedAt(now.GetTheInstant());
        data.setLastModifiedAt(now.GetTheInstant());

    }

    void UpdateDBdata(MatchEventData newData, Date modified){

        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(newData.getMatchId());

        UpdateOperations<MatchEventData> ops = getDatastore().createUpdateOperations(MatchEventData.class).set("MatchKey",newData.getMatchKey());
        getDatastore().update(query,ops);
        ops = getDatastore().createUpdateOperations(MatchEventData.class).set("homeTeam",newData.getHomeTeam());
        getDatastore().update(query,ops);
        ops = getDatastore().createUpdateOperations(MatchEventData.class).set("awayTeam",newData.getAwayTeam());
        getDatastore().update(query,ops);
        ops = getDatastore().createUpdateOperations(MatchEventData.class).set("commence",newData.getCommence());
        getDatastore().update(query,ops);

        ops = getDatastore().createUpdateOperations(MatchEventData.class).set("lastModifiedAt",modified);
        getDatastore().update(query,ops);
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

