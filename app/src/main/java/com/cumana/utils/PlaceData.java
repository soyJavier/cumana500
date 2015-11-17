package com.cumana.utils;

import com.cumana.struct.places;

import java.util.ArrayList;

/**
 * Created by Javier on 12-11-2015.
 */
public class PlaceData {

    public static ArrayList<places> listPlaces = new ArrayList<places>();

    public static places getListPlaces(int position) {
        return listPlaces.get(position);
    }
}
