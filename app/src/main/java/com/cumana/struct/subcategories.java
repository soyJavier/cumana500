package com.cumana.struct;

/**
 * Created by Javier on 15-11-2015.
 */
public class subcategories {

    public int _id;
    public String categories;
    public Boolean check;


    public subcategories(int _id,String categories,Boolean check){
        this._id = _id;
        this.categories = categories;
        this.check = check;
    }

    public Boolean getCheck() {
        return check;
    }

    public int get_id() {
        return _id;
    }

    public String getCategories() {
        return categories;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
