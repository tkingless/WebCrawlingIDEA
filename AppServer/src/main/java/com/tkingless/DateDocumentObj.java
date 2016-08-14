package com.tkingless;

import org.bson.Document;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by tkingless on 8/14/16.
 */
public class DateDocumentObj {

    public enum HistoryType {
        UPDATE_SCORE,
        UPDATE_STAGE,
        UPDATE_HAD_ODD,
        UPDATe_CHL_ODD
    }

    Date date;
    Document doc;
    HistoryType type;

    public DateDocumentObj(Date date, Document doc, HistoryType type) {
        this.date = date;
        this.doc = doc;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public Document getDoc() {
        return doc;
    }

    public HistoryType getType() {
        return type;
    }

    public static void SortByAscendOrder(List<DateDocumentObj> list){
        list.sort(Comparator.comparing (data -> data.getDate()));
    }
}
