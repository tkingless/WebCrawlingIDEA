package com.tkk.WebCrawling.DBobject;

import com.mongodb.MongoClient;
import com.tkk.MongoDBparam;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

/**
 * Created by tkingless on 7/29/16.
 */

//The Integer is Match id
public class MatchEventDAO extends BasicDAO<MatchEventData, ObjectId> {

    public MatchEventDAO(MongoClient mongoClient, Morphia morphia) {
        super(mongoClient, morphia, MongoDBparam.webCrawlingDB);
    }

    public List<MatchEventData> findAll() {
        return getDatastore().find(MatchEventData.class).asList();
    }

    public List<MatchEventData> findByMatchId (Integer id){
        return getDatastore().find(MatchEventData.class).filter("MatchId = ", id).order("MatchId").asList();
    }
}
