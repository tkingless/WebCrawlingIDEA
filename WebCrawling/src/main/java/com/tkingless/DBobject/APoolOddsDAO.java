package com.tkingless.DBobject;

import com.mongodb.MongoClient;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.MongoDBparam;
import com.tkingless.WebCrawling.DBobject.APoolOddsData;
import com.tkingless.crawlee.MatchCrawlee;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.ArrayList;
import java.util.Comparator;
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
    public void InsertOddPoolUpdates(String matchId, MatchCrawlee lastCrle, Set<MatchCONSTANTS.UpdateDifferentiator> difftr){

    }

    //CRUD: READ
    public List<APoolOddsData> GetAllOdds(String matchId, MatchCONSTANTS.InplayPoolType type){

        List<APoolOddsData> aList = new ArrayList<>();

        return aList;
    }

    public void SortUpdateRecordsByASC(){
        //TODO return time-order sorted list
        //aList.sort(Comparator.comparing(data -> data.getRecorded()));
    }

    public void updateAllOdds(APoolOddsData odds){

    }

    public void deleteAllOdds(APoolOddsData odds){

    }
}
