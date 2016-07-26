package com.tkk;
import java.util.List;

/**
 * Created by tsangkk on 7/26/16.
 */
public interface AllsOddsDAO {
    public List<AllOddsData> getAll_ALLOddsData();
    public AllOddsData getAllOdds(int matchNo);
    public void updateAllOdds(AllOddsData odds);
    public void deleteAllOdds(AllOddsData odds);
}
