package com.tkingless;

import org.bson.Document;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by tkingless on 8/14/16.
 */
public class DateDocumentObj {

    Date date;
    Document doc;

    public DateDocumentObj(Date date, Document doc) {
        this.date = date;
        this.doc = doc;
    }

    public Date getDate() {
        return date;
    }

    public Document getDoc() {
        return doc;
    }

    public static void SortByAscendOrder(List<DateDocumentObj> list){
        list.sort(Comparator.comparing (data -> data.getDate()));
    }
}
