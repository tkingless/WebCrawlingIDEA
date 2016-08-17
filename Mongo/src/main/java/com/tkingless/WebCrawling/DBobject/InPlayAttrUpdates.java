package com.tkingless.WebCrawling.DBobject;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

import static org.mongodb.morphia.utils.IndexType.DESC;

/**
 * Created by tkingless on 8/1/16.
 */

@Entity("InPlayAttrUpdates")
@Indexes({
        //@Index(value = "recorded", fields = @Field(value = "recorded", type = ASC), options = @IndexOptions(unique = true) )
        @Index(value = "recorded", fields = @Field(value = "recorded", type = DESC), options = @IndexOptions(unique = false)),
        @Index(value = "MatchId", fields = @Field(value = "MatchId", type = DESC), options = @IndexOptions(unique = false))
})

public class InPlayAttrUpdates {

    @Id
    ObjectId id;
    @Property("MatchId")
    Integer MatchId;
    @Property("recorded")
    Date recorded;
    String val;
    String type;

    public Date getRecorded() {
        return recorded;
    }

    public void setRecorded(Date recorded) {
        this.recorded = recorded;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMatchId() {
        return MatchId;
    }

    public void setMatchId(Integer matchId) {
        MatchId = matchId;
    }
}
