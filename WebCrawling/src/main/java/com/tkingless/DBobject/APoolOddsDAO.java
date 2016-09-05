package com.tkingless.DBobject;

import com.mongodb.MongoClient;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.WebCrawling.DBobject.APoolOddsData;
import com.tkingless.crawlee.MatchCrawlee;
import com.tkingless.utils.logTest;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tsangkk on 7/26/16.
 */

public class APoolOddsDAO extends BasicDAO<APoolOddsData, ObjectId> {

    public APoolOddsDAO(Datastore ds) {
        super(ds);
    }

    public APoolOddsDAO(MongoClient mongoClient, Morphia morphia, String DBname){
        super(mongoClient, morphia, DBname);
        getDatastore().ensureIndexes();
    }

    //CRUD: CREATE

    //this function coupled with MatchCrawlee::ExplainHADpool(), ExplainCHIpool(), ExtractPoolTypeBody()
    public void InsertOddPoolUpdates(Integer matchId, MatchCrawlee lastCrle, MatchCONSTANTS.InplayPoolType aPool) {

        try {
            APoolOddsData data = new APoolOddsData();
            data.setMatchId(matchId);
            data.setRecorded(lastCrle.getRecordTime().GetTheInstant());

            switch (aPool) {
                case HAD:
                    HashMap<String, String> toUpdateHAD = lastCrle.ExtractPoolTypeBody(MatchCONSTANTS.InplayPoolType.HAD);
                    if(toUpdateHAD.get("Exist").equals("true")) {
                        data.setPoolStatus(toUpdateHAD.get("PoolStat"));
                        data.setType("HAD");
                        data.setHADhomeOdd(Double.parseDouble(toUpdateHAD.get("home")));
                        data.setHADdrawOdd(Double.parseDouble(toUpdateHAD.get("draw")));
                        data.setHADawayOdd(Double.parseDouble(toUpdateHAD.get("away")));
                        save(data);
                    }
                    break;
                case CHL:
                    HashMap<String, String> toUpdateCHL = lastCrle.ExtractPoolTypeBody(MatchCONSTANTS.InplayPoolType.CHL);
                    if(toUpdateCHL.get("Exist").equals("true")) {
                        data.setType("CHL");
                        data.setPoolStatus(toUpdateCHL.get("PoolStat"));
                        data.setCHLline_1(toUpdateCHL.get("CHLline_1"));
                        data.setCHLhigh_1(Double.parseDouble(toUpdateCHL.get("CHLhigh_1")));
                        data.setCHLlow_1(Double.parseDouble(toUpdateCHL.get("CHLlow_1")));

                        if(toUpdateCHL.containsKey("CHLline_2")){
                            data.setCHLline_2(toUpdateCHL.get("CHLline_2"));
                            data.setCHLhigh_2(Double.parseDouble(toUpdateCHL.get("CHLhigh_2")));
                            data.setCHLlow_2(Double.parseDouble(toUpdateCHL.get("CHLlow_2")));
                        }

                        save(data);
                    }
                    break;
                default:
                    logTest.logger.error("[PoolOddInsertion] Undefined pool yet");
                    return;
            }

        }catch (Exception e){
            logTest.logger.error("pool error: ", e);
        }
    }

    //CRUD: READ
    public List<APoolOddsData> GetAllOdds(String matchId, MatchCONSTANTS.InplayPoolType type){

        List<APoolOddsData> aList = getDatastore().find(APoolOddsData.class).field("MatchId").equal(matchId).field("type").equal(MatchCONSTANTS.GetCapPoolType(type)).asList();

        return aList;
    }

    public void SortUpdateRecordsByASC(){
        //aList.sort(Comparator.comparing(data -> data.getRecorded()));
    }

    public void updateAllOdds(APoolOddsData odds){

    }

    public void deleteAllOdds(APoolOddsData odds){

    }
}
