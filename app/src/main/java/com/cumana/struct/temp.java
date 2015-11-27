package com.cumana.struct;

import android.content.ContentValues;

/**
 * Created by Javier on 23-11-2015.
 */
public class temp {

    private int table;
    private String url,type,_id;



    public temp(){
    }

    public temp(String _id,int table,String url,String type){
        this._id = _id;
        this.table = table;
        this.url = url;
        this.type = type;
    }


    public void set_id(String _id) {
        this._id = _id;
    }


    public void setTable(int table) {
        this.table = table;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String get_id() {
        return _id;
    }

    public int getTable() {
        return table;
    }


    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public ContentValues setInfo(){

        ContentValues values = new ContentValues();

        values.put("_id",get_id());
        values.put("url",getUrl());
        values.put("type",getType());
        values.put("tabla",getTable());

        return values;
    }

}
