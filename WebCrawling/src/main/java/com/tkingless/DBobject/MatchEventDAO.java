package com.tkingless.DBobject;

import com.mongodb.MongoClient;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.MongoDBparam;
import com.tkingless.WebCrawling.DBobject.InPlayAttrUpdates;
import com.tkingless.WebCrawling.DBobject.MatchEventData;
import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.logTest;
import com.tkingless.webCrawler.MatchEventWorker;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.*;

/**
 * Created by tkingless on 7/29/16.
 */

//The Integer is Match id
public class MatchEventDAO extends BasicDAO<MatchEventData, ObjectId> {

    InplayAttrDAO inplayAttrDAO;

    public MatchEventDAO(Datastore ds) {
        super(ds);
        inplayAttrDAO = new InplayAttrDAO(ds);
    }

    public MatchEventDAO(MongoClient mongoClient, Morphia morphia, String DBname){
        super(mongoClient, morphia, DBname);
        getDatastore().ensureIndexes();
        inplayAttrDAO = new InplayAttrDAO(mongoClient, morphia, DBname);
    }

    //CRUD: create

    public void RegisterMatchEventWorker (MatchEventWorker worker) {
        String idStr = worker.getMatchId();
        if(!IsMatchRegisteredBefore(idStr)){
            SaveMatchEventWorker(worker);
        }else{

            //Workaround
            if(worker.getCommenceTime() == null){
                logTest.logger.error("commence is null for on matchingEvent, not expected usage.");
                return;
            }

            MatchEventData DBdata = findByMatchId(idStr);
            MatchEventData grabbedData = new MatchEventData();
            EventWorkerToDBdata(worker,grabbedData);

            if(!DBdata.equals(grabbedData)){
                UpdateDBdata(grabbedData,worker.getWorkerTime().GetTheInstant());

            }
        }
    }

    private void EventWorkerToDBdata(MatchEventWorker worker, MatchEventData data){
        data.setMatchId(Integer.parseInt(worker.getMatchId()));
        data.setMatchKey(worker.getMatchKey());
        data.setHomeTeam(worker.getHomeTeam());
        data.setAwayTeam(worker.getAwayTeam());

        if(worker.getCommenceTime() != null){
            data.setCommence(worker.getCommenceTime().GetTheInstant());
        }

        DateTimeEntity now = new DateTimeEntity();
        data.setCreatedAt(now.GetTheInstant());
        data.setLastModifiedAt(now.GetTheInstant());

    }

    private void SaveMatchEventWorker(MatchEventWorker worker){
        MatchEventData data = new MatchEventData();
        EventWorkerToDBdata(worker,data);
        this.save(data);
    }

    //CRUD: read

    public MatchEventData findByMatchId (String id){
        return findOne("MatchId",Integer.parseInt(id));
    }

    public boolean IsMatchRegisteredBefore(String id){
        return exists("MatchId",Integer.parseInt(id));
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

    public List<MatchEventData> findAll() {
        return getDatastore().find(MatchEventData.class).asList();
    }

    public Set<MatchCONSTANTS.InplayPoolType> QueryPoolTypes(MatchEventWorker worker){
        Set<MatchCONSTANTS.InplayPoolType> returnPools;

        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(Integer.parseInt(worker.getMatchId()));
        MatchEventData data = query.get();
        returnPools = MatchCONSTANTS.GetInplayPoolTypeSet(data.getPoolTypes());

        return returnPools;
    }

    //CRUD: update

    public void UpdateInplayStage(MatchEventWorker worker,InPlayAttrUpdates updates){
        //Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(Integer.parseInt(worker.getMatchId()));
        //UpdateOperations<MatchEventData> ops = getDatastore().createUpdateOperations(MatchEventData.class).add(ArrayField,val);
        //getDatastore().update(query,ops);
        MatchEventData matchData = findByMatchId(worker.getMatchId());

        inplayAttrDAO.save(updates);
        matchData.getStageUpdates().add(updates);

        getDatastore().save(matchData);
        ApplyLastModified(worker);
    }

    public void UpdateInplayScore(MatchEventWorker worker,InPlayAttrUpdates updates){

        MatchEventData matchData = findByMatchId(worker.getMatchId());

        inplayAttrDAO.save(updates);
        matchData.getStageUpdates().add(updates);

        getDatastore().save(matchData);
        ApplyLastModified(worker);
    }

    public void UpdateInplayCorner(MatchEventWorker worker,InPlayAttrUpdates updates){

        MatchEventData matchData = findByMatchId(worker.getMatchId());

        inplayAttrDAO.save(updates);
        matchData.getCornerTotUpdates().add(updates);

        getDatastore().save(matchData);
        ApplyLastModified(worker);
    }

    private void UpdateDBdata(MatchEventData newData, Date modified){
        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(newData.getMatchId());

        UpdateOperations<MatchEventData> ops = getDatastore().createUpdateOperations(MatchEventData.class).set("MatchKey",newData.getMatchKey());
        getDatastore().update(query,ops);
        ops = getDatastore().createUpdateOperations(MatchEventData.class).set("homeTeam",newData.getHomeTeam());
        getDatastore().update(query,ops);
        ops = getDatastore().createUpdateOperations(MatchEventData.class).set("awayTeam",newData.getAwayTeam());
        getDatastore().update(query,ops);
        ops = getDatastore().createUpdateOperations(MatchEventData.class).set("commence",newData.getCommence());
        getDatastore().update(query,ops);
        ApplyLastModified(newData,modified);
    }

    public void SetField(MatchEventWorker worker, String field, Object val){
        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(Integer.parseInt(worker.getMatchId()));
        UpdateOperations<MatchEventData> ops = getDatastore().createUpdateOperations(MatchEventData.class).set(field,val);
        getDatastore().update(query,ops);

        ApplyLastModified(worker);
    }

    private void ApplyLastModified(MatchEventWorker worker){
        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(Integer.parseInt(worker.getMatchId()));
        UpdateOperations<MatchEventData> ops = getDatastore().createUpdateOperations(MatchEventData.class).set("lastModifiedAt",worker.getLastModifiedTime().GetTheInstant());
        getDatastore().update(query,ops);
    }

    private void ApplyLastModified(MatchEventData data, Date mod){
        Query<MatchEventData> query = getDatastore().createQuery(MatchEventData.class).field("MatchId").equal(data.getMatchId());
        UpdateOperations<MatchEventData> ops = getDatastore().createUpdateOperations(MatchEventData.class).set("lastModifiedAt",mod);
        getDatastore().update(query,ops);
    }

    //CRUD: delete

}

