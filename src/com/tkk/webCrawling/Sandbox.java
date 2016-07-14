package com.tkk.webCrawling;

import com.tkk.webCrawling.utils.MapComparator;

import java.util.HashMap;

/**
 * Created by tsangkk on 7/13/16.
 */
public class Sandbox {

    public static void main(String[] args) {

       /* DateTimeEntity nowDate = new DateTimeEntity();

        System.out.println("nowDate: " + nowDate.GetTheInstantTime());
        long timestamp = nowDate.GetTheInstant().getTime();

        timestamp -= 1000 * 60 * 3 + 1000 * 30;

        DateTimeEntity newDate = new DateTimeEntity(timestamp);

        System.out.println("newDate: " + newDate.GetTheInstantTime());*/

        HashMap<String,String> hmapA = new HashMap<String, String>();
        HashMap<String,String> hmapB = new HashMap<String, String>();

        System.out.println(MapComparator.CompareMapsDifferent(hmapA,hmapB));

        hmapA.put("Akey","Aval");
        hmapB.put("Akey","Aval");
        System.out.println(MapComparator.CompareMapsDifferent(hmapA,hmapB));

        hmapA.put("Bkey","Bval");
        System.out.println(MapComparator.CompareMapsDifferent(hmapA,hmapB));

        hmapA.remove("Bkey");
        hmapA.put("Akey","Bval");
        System.out.println(MapComparator.CompareMapsDifferent(hmapA,hmapB));

    }
}
