package com.cumana.tables;

import android.content.ContentValues;

/**
 * Created by Javier on 22-10-2015.
 */
public class ciudad {

    private String _id;
    private String name;
    private String history;
    private String personajes;

    public ciudad(){
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setPersonajes(String personajes) {
        this.personajes = personajes;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public String getHistory() {
        return history;
    }

    public String getPersonajes() {
        return personajes;
    }

    public ContentValues setCiudad(){
        ContentValues values = new ContentValues();

        values.put("_id",get_id());
        values.put("nombre",getName());
        values.put("historia",getHistory());
        values.put("personajes",getPersonajes());

        return values;
    }
}
