package com.tkingless.utils;

import java.util.Map;

/**
 * Created by tsangkk on 7/14/16.
 */
public class MapComparator {

    public static boolean CompareMapsDifferent(Map<String,String> mapA, Map<String,String>  mapB){
        if(mapA == null || mapB == null){
            return false;
        }

        if(mapA.isEmpty() || mapB.isEmpty()){
            return false;
        }

        try{
            for(String k : mapB.keySet()){
                if(!mapA.get(k).equals(mapB.get(k)) ){
                    return true;
                }
            }
            for(String y : mapA.keySet()){
                if(!mapB.containsKey(y)){
                    return true;
                }
            }
        } catch (NullPointerException np){
            return true;
        }
        return false;
    }
}
