package com.cumana.list;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cumana.adapters.EndlessRecyclerOnScrollListener;
import com.cumana.adapters.adapter_list;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.maps.maps;
import com.cumana.request.RequestJsonArray;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.struct.places;
import com.cumana.utils.PlaceData;
import com.cumana.utils.utils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import tr.xip.errorview.ErrorView;

/**
 * Created by Javier on 21-10-2015.
 */
public class list extends AppCompatActivity{

    ActionBar actionBar;
    Toolbar toolbar;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    //private RecyclerView.LayoutManager lManager;
    private LinearLayoutManager lManager;
    List items = new ArrayList();
    JSONArray json,jsonGeneral;
    int position=0;
    TextView title;
    CircleProgressBar loading;
    ErrorView error;
    SQLiteHelper sqlite;

    private int overallXScroll = 0;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lManager = new LinearLayoutManager(this);
        loading = (CircleProgressBar) findViewById(R.id.loading);
        error = (ErrorView) findViewById(R.id.error);

        sqlite = SQLiteHelper.getHelper(this);

        setSupportActionBar(toolbar);
        jsonGeneral = new JSONArray();

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        title = (TextView) toolbar.findViewById(R.id.title);

        title.setText(getIntent().getExtras().getString("title"));
        title.setVisibility(View.VISIBLE);

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        //lManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {

                            Intent intent = new Intent(list.this, details.class);
                            intent.putExtra("details", jsonGeneral.getString(position));
                            intent.putExtra("position", position);

                            ImageView placeImage = (ImageView) view.findViewById(R.id.image);
                            TextView placeNameHolder = (TextView) view.findViewById(R.id.name);

                            Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");
                            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");

                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(list.this,
                                    imagePair, holderPair);
                            ActivityCompat.startActivity(list.this, intent, options.toBundle());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
        );

        recycler.addOnScrollListener(new EndlessRecyclerOnScrollListener(lManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (json.length() >= 10) {
                    Log.w("pido", "si pido mas");
                    position = position + 10;
                    donwloadInfo();
                } else {
                    Log.w("pido", "no pido mas: " + json.length());
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dy;
            }
        });

        if(getIntent().getExtras().containsKey("json")){


            try{

                json = new JSONArray(getIntent().getExtras().getString("json"));

                for(int i=0;i<json.length();i++){
                    jsonGeneral.put(new JSONObject(json.getJSONObject(i).toString()));
                    new PlaceData().listPlaces.add(new places(json.getJSONObject(i).getString("id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getString("subcategory"),json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),false,getApplicationContext()));
                    items.add(new places(json.getJSONObject(i).getString("id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getString("subcategory"),json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),false,getApplicationContext()));
                }

                if(!isFinishing()) {
                    adapter = new adapter_list(items);
                    if(jsonGeneral.length() == json.length()) {
                        recycler.setAdapter(adapter);
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                    loading.setVisibility(View.GONE);
                    recycler.scrollToPosition(overallXScroll);
                    ((LinearLayoutManager) recycler.getLayoutManager()).scrollToPosition(overallXScroll);
                }

            }catch(JSONException e){

                e.printStackTrace();
                loading.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        }else {
            donwloadInfo();
        }

        error.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                error.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                donwloadInfo();
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.icon_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void donwloadInfo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new utils().url+"places/category/"+sqlite.getCiudad().get(0).get_id()+"/"+getIntent().getExtras().getInt("category")+"/"+position+"";
        Log.w("url",url);

        RequestJsonArray x = new RequestJsonArray(Request.Method.GET,url,json,new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                try {

                    json = response;

                    for(int i=0;i<json.length();i++){
                        jsonGeneral.put(new JSONObject(json.getJSONObject(i).toString()));
                        if(getIntent().getExtras().getInt("category") == 1){
                            new PlaceData().listPlaces.add(new places(json.getJSONObject(i).getString("id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getString("autority"), json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),true,getApplicationContext()));
                            items.add(new places(json.getJSONObject(i).getString("id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getString("autority"), json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),true,getApplicationContext()));
                        }else{
                            new PlaceData().listPlaces.add(new places(json.getJSONObject(i).getString("id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getString("subcategory"),json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),false,getApplicationContext()));
                            items.add(new places(json.getJSONObject(i).getString("id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getString("subcategory"),json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),false,getApplicationContext()));
                        }
                    }

                    if(!isFinishing()) {
                        adapter = new adapter_list(items);
                        if(jsonGeneral.length() == json.length()) {
                            recycler.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        loading.setVisibility(View.GONE);
                        recycler.scrollToPosition(overallXScroll);
                        ((LinearLayoutManager) recycler.getLayoutManager()).scrollToPosition(overallXScroll);
                    }

                }catch(JSONException e){

                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.w("error",e+"");
                loading.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        });

        x.setRetryPolicy(new DefaultRetryPolicy(
                16000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(x);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.search:{
                Intent move = new Intent();
                move.setClass(getApplicationContext(),search.class);
                startActivity(move);
                return true;
            }

            case R.id.map:{
                Intent move = new Intent();
                move.setClass(getApplicationContext(),maps.class);
                move.putExtra("json",jsonGeneral.toString());
                startActivity(move);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }



}
