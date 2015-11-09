package com.cumana.list;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.cumana.struct.places;
import com.cumana.utils.utils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import org.json.JSONArray;
import org.json.JSONException;

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
    final Handler handler = new Handler();
    JSONArray json;
    int position=0;
    TextView title;
    CircleProgressBar loading;
    ErrorView error;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lManager = new LinearLayoutManager(this);
        loading = (CircleProgressBar) findViewById(R.id.loading);
        error = (ErrorView) findViewById(R.id.error);

        setSupportActionBar(toolbar);

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
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            Intent move = new Intent();
                            move.setClass(getApplicationContext(), details.class);
                            move.putExtra("details", json.getString(position));
                            startActivity(move);
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
                    Log.w("pido","si pido mas");
                    position = position + 10;
                    donwloadInfo();
                }else{
                    Log.w("pido","no pido mas: "+json.length());
                }

            }
        });

        donwloadInfo();

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
        String url = new utils().url+"places/cum/"+getIntent().getExtras().getString("category")+"/"+position+"/10";
        Log.w("url",url);

        RequestJsonArray x = new RequestJsonArray(Request.Method.GET,url,json,new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                try {

                    json = response;

                    for(int i=0;i<json.length();i++){

                        if(getIntent().getExtras().getString("category").equals("institucion")){

                            if(!json.getJSONObject(i).isNull("autority")){
                                items.add(new places(json.getJSONObject(i).getString("_id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getString("autority"), json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),true,getApplicationContext()));
                            }else{
                                items.add(new places(json.getJSONObject(i).getString("_id"), json.getJSONObject(i).getString("name"),null, json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),true,getApplicationContext()));
                            }
                        }else{
                            items.add(new places(json.getJSONObject(i).getString("_id"), json.getJSONObject(i).getString("name"),json.getJSONObject(i).getJSONArray("subcategory").getJSONObject(0).getString("name"),json.getJSONObject(i).getString("description"),json.getJSONObject(i).getJSONArray("images").getString(0),false,getApplicationContext()));
                        }
                    }

                    if(!isFinishing()) {
                        adapter = new adapter_list(items);
                        recycler.setAdapter(adapter);
                        loading.setVisibility(View.GONE);
                    }

                }catch(JSONException e){
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

        queue.add(x);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.search:{

                return true;
            }

            case R.id.map:{
                Intent move = new Intent();
                move.setClass(getApplicationContext(),maps.class);
                move.putExtra("json",json.toString());
                startActivity(move);
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }



}
