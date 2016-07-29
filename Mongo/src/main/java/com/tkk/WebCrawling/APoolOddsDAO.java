package com.tkk.WebCrawling;
import java.util.List;

/**
 * Created by tsangkk on 7/26/16.
 */
public interface APoolOddsDAO {
    public List<APoolOddsData> getAll_ALLOddsData();
    public APoolOddsData getAllOdds(int matchNo);
    public void updateAllOdds(APoolOddsData odds);
    public void deleteAllOdds(APoolOddsData odds);
}
