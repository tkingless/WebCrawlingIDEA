package com.tkk.webCrawling;

import com.tkk.webCrawling.utils.DateTimeEntity;

/**
 * Created by tsangkk on 7/13/16.
 */
public class Sandbox {

    public static void main(String[] args) {

        DateTimeEntity nowDate = new DateTimeEntity();

        System.out.println("nowDate: " + nowDate.GetTheInstantTime());
        long timestamp = nowDate.GetTheInstant().getTime();

        timestamp -= 1000 * 60 * 3 + 1000 * 30;

        DateTimeEntity newDate = new DateTimeEntity(timestamp);

        System.out.println("newDate: " + newDate.GetTheInstantTime());
    }
}
