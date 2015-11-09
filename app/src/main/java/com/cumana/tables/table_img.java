package com.cumana.tables;

import android.content.ContentValues;

/**
 * Created by Javier on 22-10-2015.
 */
public class table_img {

    private String _id;
    private String img;
    private String type;

    public table_img(){
    }

    public table_img(String _id,String img){
        this._id = _id;
        this.img = img;
    }

    public table_img(String _id,String img,String type){
        this._id = _id;
        this.img = img;
        this.type = type;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public String getImg() {
        return img;
    }

    public String getType() {
        return type;
    }

    public ContentValues setImageType(){
        ContentValues values = new ContentValues();

        values.put("_id",get_id());
        values.put("tipo",getType());
        values.put("image",getImg());

        return values;
    }

    public ContentValues setImage(){
        ContentValues values = new ContentValues();

        values.put("_id",get_id());
        values.put("image",getImg());

        return values;
    }

    @Override
    public String toString() {
        return "{_id:"+this._id+",img:"+this.img+"}";
    }
}
