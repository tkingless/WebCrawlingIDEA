package com.tkingless;

import com.tkingless.WebCrawling.DBobject.APoolOddsData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by tkingless on 7/30/16.
 */
public class Miscs {

    @Test
    public void TestStringSplit() throws Exception {
        String teams = "asdfoivsoeio";
        String[] parts = teams.split("vs");

        for(String part: parts){
           System.out.println(part);
        }
    }

    @Test
    public void TestDateSortedList() throws Exception {
        List<APoolOddsData> sortedList = new ArrayList<>();

        Date middle = new Date((1470190376)*1000);
        Date first = new Date((1470190376-1000)*1000);
        Date later = new Date((1470190376+1000)*1000);

        APoolOddsData data = new APoolOddsData();

        data.setRecorded(middle);
        sortedList.add(data);
        data = new APoolOddsData();
        data.setRecorded(later);
        sortedList.add(data);
        data = new APoolOddsData();
        data.setRecorded(first);
        sortedList.add(data);

        for(APoolOddsData Adata : sortedList){
            System.out.println(Adata.getRecorded());
        }

        //ASC ORDER
        //sortedList.sort(Comparator.comparing (data -> data.getRecorded()));

        //DESC ORDER
        Comparator<APoolOddsData> comparator = (h1, h2) -> h1.getRecorded().compareTo(h2.getRecorded());
        sortedList.sort(comparator.reversed());

        for(APoolOddsData Adata : sortedList){
            System.out.println(Adata.getRecorded());
        }



    }
}
