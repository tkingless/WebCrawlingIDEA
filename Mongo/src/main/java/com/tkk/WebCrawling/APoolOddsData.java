package com.tkk.WebCrawling;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

import static org.mongodb.morphia.utils.IndexType.DESC;

/**
 * Created by tsangkk on 6/27/16.
 */

@Entity("AllOdds")
@Indexes({
        @Index(value = "MatchId", fields = @Field(value = "MatchdId", type = DESC)),
        @Index(value = "recorded", fields = @Field(value = "recorded", type = DESC))
})

public class APoolOddsData {

    @Id
    private ObjectId id;
    private int MatchId;
    private String type;

    private Double HADhomeOdd;
    private Double HADdrawOdd;
    private Double HADawayOdd;
    private Double CHLline;
    private Double CHLhigh;
    private Double CHLlow;

    private String poolStatus;

    private Date recorded;
}
