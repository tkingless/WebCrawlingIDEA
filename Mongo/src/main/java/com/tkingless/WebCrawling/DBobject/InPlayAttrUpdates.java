package com.tkingless.WebCrawling.DBobject;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

import static org.mongodb.morphia.utils.IndexType.ASC;

/**
 * Created by tkingless on 8/1/16.
 */

@Entity("InPlayAttrUpdates")
@Indexes({
        @Index(value = "time", fields = @Field(value = "time", type = ASC), options = @IndexOptions(unique = true) )
})

@Embedded
public class InPlayAttrUpdates {

    @Id
    ObjectId id;
    Date time;
    String val;
    String updateType;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
}
