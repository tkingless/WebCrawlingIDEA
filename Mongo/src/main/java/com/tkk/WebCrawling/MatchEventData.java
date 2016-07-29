package com.tkk.WebCrawling;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.mongodb.morphia.utils.IndexType.DESC;

/**
 * Created by tkingless on 7/29/16.
 */

@Entity("MatchEvents")
@Indexes({
        @Index(value = "MatchId", fields = @Field(value = "MatchdId", type = DESC)),
        @Index(value = "createdAt", fields = @Field(value = "createdAt", type = DESC))
})

public class MatchEventData {

    @Id
    private ObjectId id;
    private Integer MatchId;
    private String MatchKey;
    private HashMap<Date,String> stageUpdates;
    private HashMap<Date,String> scoreUpdate;
    private HashMap<Date,Integer> cornerTotUpdate;

    private String homeTeam;
    private String awayTeam;
    private List<String> poolTypes;

    private Date commence;
    private Date actualCommence;
    private Date endTime;
    private Date createdAt;
    private Date lastModifiedAt;

}
