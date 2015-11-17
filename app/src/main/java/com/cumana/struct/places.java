package com.cumana.struct;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Javier on 21-10-2015.
 */
public class places {

    private String _id;
    private String name;
    private String description;
    private String subName;
    private String url;
    private Boolean autority;
    private Context ctx;
    private Bitmap bitmap;

    public places(String _id,String name,String subName,String description,String url,Boolean autority,Context ctx){
        this.url = url;
        this._id = _id;
        this.name = name;
        this.subName = subName;
        this.description = description;
        this.autority = autority;
        this.ctx = ctx;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String get_id() {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getSubName() {
        return subName;
    }

    public Boolean getAutority() {
        return autority;
    }

    public String getUrl() {
        return url;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAutority(Boolean autority) {
        this.autority = autority;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Context getCtx() {
        return ctx;
    }

    @Override
    public String toString() {
        return "{_id:"+this._id+",name:"+this.name+",subName:"+this.subName+",description:"+this.description+",url:"+this.url+",autority:"+this.autority+"}";
    }
}
