package com.cumana.tables;

import android.content.ContentValues;

/**
 * Created by Javier on 22-10-2015.
 */
public class clima {
    String contenido;

    public clima(String contenido){
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public ContentValues setClima(){
        ContentValues values = new ContentValues();

        values.put("clima",getContenido());

        return values;
    }

    public ContentValues setCategory(){
        ContentValues values = new ContentValues();

        values.put("categorias",getContenido());

        return values;
    }

    @Override
    public String toString() {
        return "{category:"+this.contenido+"}";
    }
}
