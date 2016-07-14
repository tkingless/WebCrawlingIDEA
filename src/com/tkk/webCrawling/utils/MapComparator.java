package com.tkk.webCrawling.utils;

import java.util.Map;

/**
 * Created by tsangkk on 7/14/16.
 */
public class MapComparator {

    public static boolean CompareMaps(Map<String,String> mapA, Map<String,String>  mapB){
        if(mapA == null || mapB == null){
            return false;
        }

        if(mapA.isEmpty() || mapB.isEmpty()){
            return false;
        }

        try{
            for(String k : mapB.keySet()){
                if(mapA.get(k) != mapB.get(k)){
                    return false;
                }
            }
            for(String y : mapA.keySet()){
                if(!mapB.containsKey(y)){
                    return false;
                }
            }
        } catch (NullPointerException np){
            return false;
        }
        return true;
    }
}
