package com.tkingless.DBobject;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.MongoDBparam;
import com.tkingless.WebCrawling.DBobject.APoolOddsData;
import com.tkingless.crawlee.MatchCrawlee;
import com.tkingless.utils.logTest;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.HashMap;
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

    //this function coupled with MatchCrawlee::ExplainHADpool(), ExplainCHIpool(), ExtractPoolTypeBody()
    public void InsertOddPoolUpdates(String matchId, MatchCrawlee lastCrle, Set<MatchCONSTANTS.UpdateDifferentiator> difftr) throws XPathExpressionException {

        for (MatchCONSTANTS.UpdateDifferentiator update : difftr){

            APoolOddsData data = new APoolOddsData();
            data.setMatchId(Integer.parseInt(matchId));
            data.setRecorded(lastCrle.getRecordTime().GetTheInstant());

            switch (update){
                case UPDATE_POOL_HAD:
                    HashMap<String,String> toUpdateHAD = lastCrle.ExtractPoolTypeBody(MatchCONSTANTS.InplayPoolType.HAD);
                    data.setType("HAD");
                    data.setPoolStatus(toUpdateHAD.get("PoolStat"));
                    data.setHADhomeOdd(Double.parseDouble(toUpdateHAD.get("home")));
                    data.setHADdrawOdd(Double.parseDouble(toUpdateHAD.get("draw")));
                    data.setHADawayOdd(Double.parseDouble(toUpdateHAD.get("away")));
                    break;
                case UPDATE_POOL_CHL:
                    HashMap<String,String> toUpdateCHL = lastCrle.ExtractPoolTypeBody(MatchCONSTANTS.InplayPoolType.CHL);
                    data.setType("CHL");
                    data.setPoolStatus(toUpdateCHL.get("PoolStat"));
                    data.setCHLline(toUpdateCHL.get("line"));
                    data.setCHLhigh(Double.parseDouble(toUpdateCHL.get("high")));
                    data.setCHLlow(Double.parseDouble(toUpdateCHL.get("low")));
                    break;
                default:
                    logTest.logger.error("[PoolOddInsertion] Undefined pool yet");
                    break;
            }

            save(data);
        }
    }

    //this function coupled with MatchCrawlee::ExplainHADpool(), ExplainCHIpool(), ExtractPoolTypeBody()
    public void InsertOddPoolUpdates(String matchId, MatchCrawlee lastCrle, MatchCONSTANTS.InplayPoolType aPool) {

        try {
            APoolOddsData data = new APoolOddsData();
            data.setMatchId(Integer.parseInt(matchId));
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
                        data.setCHLline(toUpdateCHL.get("line"));
                        data.setCHLhigh(Double.parseDouble(toUpdateCHL.get("high")));
                        data.setCHLlow(Double.parseDouble(toUpdateCHL.get("low")));
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
        //TODO return time-order sorted list
        //aList.sort(Comparator.comparing(data -> data.getRecorded()));
    }

    public void updateAllOdds(APoolOddsData odds){

    }

    public void deleteAllOdds(APoolOddsData odds){

    }
}
