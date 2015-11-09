package com.cumana.maps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cumana.cumana500.R;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.tables.clima;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class f_maps extends Fragment {

    View view;
    JSONArray json;
    GoogleMap mapa;
    SQLiteHelper sqlite;
    DisplayMetrics displaymetrics = new DisplayMetrics();
    DisplayMetrics metrics;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();



    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.f_maps, container, false);

       metrics = new DisplayMetrics();
       getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
       getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
       sqlite = SQLiteHelper.getHelper(getActivity());

       //category = sqlite.getCategory();

       setHasOptionsMenu(true);
       mapa =  ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps)).getMap();

        if(getArguments().containsKey("single")){

        }else{
            try {
                json = new JSONArray(getArguments().getString("json"));

                for(int i=0;i<json.length();i++){

                    builder.include(new LatLng(json.getJSONObject(i).getDouble("lat"), json.getJSONObject(i).getDouble("lon")));
                    MarkerOptions m = new MarkerOptions();
                    m.position(new LatLng(json.getJSONObject(i).getDouble("lat"), json.getJSONObject(i).getDouble("lon")));

                    mapa.addMarker(m);
                }

                mapa.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), displaymetrics.widthPixels, displaymetrics.heightPixels, (int) (Math.min(view.getWidth(), view.getHeight()) * 0.1)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }


       return view;
    }
}
