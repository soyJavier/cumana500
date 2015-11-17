package com.cumana.struct;

import com.cumana.cumana500.R;

import java.util.ArrayList;

/**
 * Created by Javier on 12-11-2015.
 */
public class services {

    int icon;
    String name;

   /* ArrayList<Integer> service = new ArrayList<Integer>(){{add(R.mipmap.ic_bar);add(R.mipmap.ic_gym);add(R.mipmap.ic_wifi);add(R.mipmap.ic_tenis);}};
    ArrayList<String> serviceName = new ArrayList<String>(){{add("Lobby bar");add("Wifi");add("Tenis");}};*/

    public services(int icon,String name){
        this.icon = icon;
        this.name = name;
    }


    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "{name:"+this.name+",icon:"+this.icon+"}";
    }
}
