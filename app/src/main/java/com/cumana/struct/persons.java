package com.cumana.struct;

/**
 * Created by Javier on 23-10-2015.
 */
public class persons {

    private String name;
    private String photo;
    private String profession;
    private String description;
    private String resumen;
    private String _id;
    private int position;


    public persons(){
    }

    public persons(String _id,String name,String photo,String profession,String description,String resumen,int position){
        this._id = _id;
        this.name = name;
        this.photo = photo;
        this.profession = profession;
        this.description = description;
        this.resumen = resumen;
        this.position = position;
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

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getProfession() {
        return profession;
    }

    public String getResumen() {
        return resumen;
    }

    public int getPosition() {
        return position;
    }
}
