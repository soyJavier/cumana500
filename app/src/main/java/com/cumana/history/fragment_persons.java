package com.cumana.history;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cumana.adapters.adapter_list;
import com.cumana.adapters.adapter_persons;
import com.cumana.cumana500.R;
import com.cumana.list.RecyclerItemClickListener;
import com.cumana.list.details;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.struct.persons;
import com.cumana.tables.ciudad;
import com.cumana.tables.clima;
import com.cumana.tables.table_img;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier on 23-10-2015.
 */
public class fragment_persons extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    List items = new ArrayList();
    SQLiteHelper sqlite;

    JSONArray persons;

    List<ciudad> queryCiudad = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_person, container, false);

        sqlite = SQLiteHelper.getHelper(getActivity());

        queryCiudad = sqlite.getCiudad();

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);


        try {
            persons = new JSONArray(queryCiudad.get(0).getPersonajes());
            int position = 1;
            for(int i=0;i<persons.length();i++){

                if(position == 1){
                    position = 0;
                }else{
                    position = 1;
                }

                List<table_img> pmg = sqlite.getPersons(persons.getJSONObject(i).getString("_id"));
                items.add(new persons(persons.getJSONObject(i).getString("_id"), persons.getJSONObject(i).getString("name"), pmg.get(0).getImg(), persons.getJSONObject(i).getString("profession"), persons.getJSONObject(i).getString("description"), persons.getJSONObject(i).getString("resume"),position));
            }

            adapter = new adapter_persons(items);
            recycler.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent move = new Intent();
                        move.setClass(getActivity(), details_persons.class);
                        move.putExtra("details_id",position);
                        startActivity(move);
                    }
                })
        );

        return view;
    }
}
