package com.tkingless;

import org.junit.Test;

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
}
