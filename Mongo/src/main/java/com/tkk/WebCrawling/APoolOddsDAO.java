package com.tkk.WebCrawling;
import java.util.List;

/**
 * Created by tsangkk on 7/26/16.
 */
public interface APoolOddsDAO {
    public List<APoolOdds> getAll_ALLOddsData();
    public APoolOdds getAllOdds(int matchNo);
    public void updateAllOdds(APoolOdds odds);
    public void deleteAllOdds(APoolOdds odds);
}
