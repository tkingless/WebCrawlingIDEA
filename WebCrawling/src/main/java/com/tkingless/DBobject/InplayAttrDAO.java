package com.tkingless.DBobject;

import com.mongodb.MongoClient;
import com.tkingless.WebCrawling.DBobject.InPlayAttrUpdates;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Created by tsangkk on 8/15/16.
 */
public class InplayAttrDAO extends BasicDAO<InPlayAttrUpdates, ObjectId> {

    protected InplayAttrDAO(Datastore ds) {
        super(ds);
    }

    public InplayAttrDAO(MongoClient mongoClient, Morphia morphia, String DBname){
        super(mongoClient, morphia, DBname);
    }
}
