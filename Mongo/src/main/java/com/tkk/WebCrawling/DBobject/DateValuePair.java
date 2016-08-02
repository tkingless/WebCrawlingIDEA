package com.tkk.WebCrawling.DBobject;

import org.mongodb.morphia.annotations.*;

import java.util.Date;

import static org.mongodb.morphia.utils.IndexType.ASC;

/**
 * Created by tkingless on 8/1/16.
 */

@Entity("DateValuePair")
@Indexes({
        @Index(value = "time", fields = @Field(value = "time", type = ASC), options = @IndexOptions(unique = true) )
})

public class DateValuePair {

    @Id
    Date time;
    String val;

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
}
