package com.tkingless.DBobject;

import com.mongodb.MongoClient;
import com.tkingless.MongoDBparam;
import com.tkingless.WebCrawling.DBobject.APoolOddsData;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

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


    /*public List<APoolOddsData> getAll_ALLOddsData(){

    }
    public APoolOddsData getAllOdds(int matchNo){

    }
    public void updateAllOdds(APoolOddsData odds){

    }
    public void deleteAllOdds(APoolOddsData odds){

    }*/
}
