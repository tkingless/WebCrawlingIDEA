package com.tkingless.DBobject;

import com.mongodb.MongoClient;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.MongoDBparam;
import com.tkingless.WebCrawling.DBobject.APoolOddsData;
import com.tkingless.crawlee.MatchCrawlee;
import com.tkingless.webCrawler.MatchEventWorker;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tsangkk on 7/26/16.
 */

public class APoolOddsDAO extends BasicDAO<APoolOddsData, ObjectId> {

    public APoolOddsDAO(MongoClient mongoClient, Morphia morphia) {
        super(mongoClient, morphia, MongoDBparam.webCrawlingDB);
    }

    public APoolOddsDAO(MongoClient mongoClient, Morphia morphia, String DBname){
        super(mongoClient, morphia, DBname);
    }

    //CRUD: CREATE
    public void InsertOddPoolUpdates(MatchEventWorker worker, MatchCrawlee lastCrle, Set<MatchCONSTANTS.UpdateDifferentiator> difftr){

    }

    //CRUD: READ
    public List<APoolOddsData> GetAllOdds(int matchNo, String poolType){
        //TODO return time-order sorted list
        List<APoolOddsData> aList = new ArrayList<>();

        return aList;
    }

    public void updateAllOdds(APoolOddsData odds){

    }

    public void deleteAllOdds(APoolOddsData odds){

    }
}
