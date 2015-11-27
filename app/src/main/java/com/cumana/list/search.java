package com.cumana.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cumana.adapters.ExpandedListView;
import com.cumana.adapters.adapter_subcategories;
import com.cumana.bitmap.converteBitmap;
import com.cumana.cumana500.R;
import com.cumana.fonts.CheckBox;
import com.cumana.fonts.EditText;
import com.cumana.maps.maps;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.struct.subcategories;
import com.cumana.utils.Keyboard;
import com.cumana.utils.utils;
import com.google.android.gms.identity.intents.AddressConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

/**
 * Created by Javier on 15-11-2015.
 */
public class search extends AppCompatActivity{

    ActionBar actionBar;
    Toolbar toolbar;
    com.cumana.fonts.TextView title;

    LinearLayout listCategories;
    SQLiteHelper sqlite;
    JSONArray categories;
    ImageView logo;
    EditText name;
    MaterialDialog dialogo;

    ArrayList<ArrayList<subcategories>> data_generals = new ArrayList<ArrayList<subcategories>>();
    ArrayList<adapter_subcategories> adapter_generals = new ArrayList<adapter_subcategories>();
    ArrayList<ExpandedListView> list_ExpandedListView = new ArrayList<ExpandedListView>();
    ArrayList<String> ids = new ArrayList<String>();
    Keyboard keyboard;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        sqlite= SQLiteHelper.getHelper(this);
        name = (EditText) findViewById(R.id.name);

        keyboard = new Keyboard(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);

        actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);

        title = (com.cumana.fonts.TextView) toolbar.findViewById(R.id.title);
        logo = (ImageView) toolbar.findViewById(R.id.logo);
        logo.setVisibility(View.GONE);

        title.setText("Filtra tu busqueda");
        title.setVisibility(View.VISIBLE);

        listCategories = (LinearLayout) findViewById(R.id.listCategories);

        LayoutInflater inf = LayoutInflater.from(this);

        try {
            categories = new JSONArray(sqlite.getCategory().get(0).getContenido());

            for(int i=0;i<categories.length();i++) {

                LinearLayout head = (LinearLayout) inf.inflate(R.layout.head_category, null, false);

                ImageView categ = (ImageView) head.findViewById(R.id.categories);
                TextView name = (TextView) head.findViewById(R.id.name);
                name.setText(categories.getJSONObject(i).getString("name"));
                categ.setImageBitmap(new converteBitmap().StringToBitMap(sqlite.getcategoryImg(4, categories.getJSONObject(i).getString("id_category")).get(0).getImg()));
                head.setContentDescription("0");
                head.setId(i);
                listCategories.addView(head);


                head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        keyboard.hide();

                        if (v.getContentDescription().toString().equals("0")) {
                            v.setContentDescription("1");
                            animate(v.findViewById(R.id.colapse)).setDuration(500).rotationX(10);
                            list_ExpandedListView.get(v.getId()).setVisibility(View.VISIBLE);
                        } else {
                            animate(v.findViewById(R.id.colapse)).setDuration(500).rotationX(180);
                            v.setContentDescription("0");
                            list_ExpandedListView.get(v.getId()).setVisibility(View.GONE);
                        }
                    }
                });


                ExpandedListView elements = new ExpandedListView(this);
                ArrayList<subcategories> sub = new ArrayList<subcategories>();
                JSONArray sb = categories.getJSONObject(i).getJSONArray("subcategory");
                for(int j=0;j<sb.length();j++){
                    sub.add(new subcategories(sb.getJSONObject(j).getInt("id_subcategory"),sb.getJSONObject(j).getString("name"),false));
                }

                data_generals.add(i, sub);

                adapter_generals.add(i, new adapter_subcategories(this,sub));
                elements.setChoiceMode(ExpandedListView.CHOICE_MODE_MULTIPLE);
                elements.setAdapter(adapter_generals.get(i));

                elements.setBackgroundColor(getResources().getColor(android.R.color.white));
                elements.setDividerHeight(0);
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 2, 0, 2);
                elements.setLayoutParams(params);
                elements.setDivider(null);
                elements.setContentDescription(i + "");
                list_ExpandedListView.add(i, elements);
                head.addView(elements);
                elements.setVisibility(View.GONE);

                elements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        keyboard.hide();

                        CheckBox check = (CheckBox) view.findViewById(R.id.check);

                        if (check.isChecked()) {
                            data_generals.get(Integer.parseInt(parent.getContentDescription().toString())).get(position).setCheck(false);
                            ids.remove(ids.indexOf("" + id));
                            Log.w("add", "remove " + id + "  -  " + ids.indexOf("" + id) + " ");
                        } else {
                            data_generals.get(Integer.parseInt(parent.getContentDescription().toString())).get(position).setCheck(true);
                            ids.add("" + id);
                            Log.w("add", "add " + id);
                        }
                        adapter_generals.get(Integer.parseInt(parent.getContentDescription().toString())).notifyDataSetChanged();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.icon_send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:{
                onBackPressed();
                return true;
            }

            case R.id.send:{

                sendSearch();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void load(JSONObject params){

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = new utils().url+"places/search";

        JsonObjectRequest req = new JsonObjectRequest(url,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.w("url", response.toString());

                        try {
                            if(response.getInt("status") == 1){

                                if (dialogo.isShowing()) {
                                    dialogo.dismiss();
                                    Intent move = new Intent();
                                    move.setClass(getApplicationContext(), list.class);
                                    move.putExtra("json", response.getJSONArray("data").toString());
                                    startActivity(move);
                                }
                            }else{
                                if (dialogo.isShowing()) {
                                    dialogo.dismiss();
                                    dialogo = new MaterialDialog.Builder(getApplication())
                                            .title("Ocurrio un error")
                                            .content("Por favor intenta de nuevo")
                                            .theme(Theme.LIGHT)
                                            .cancelable(true)
                                            .positiveText("Aceptar")
                                            .show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("response error",""+error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                26000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(req);
    }



    public void sendSearch(){
        dialogo = new MaterialDialog.Builder(this)
                .title("Buscando")
                .content("Por favor espere")
                .progress(true, 0)
                .cancelable(false)
                .theme(Theme.LIGHT)
                .cancelable(true)
                .show();


        JSONObject params = new JSONObject();

        String sb_id = "";

        for(int i=0;i<ids.size();i++){
            if(sb_id.length()==0){
                sb_id = ids.get(i);
            }else{
                sb_id += ","+ids.get(i);
            }
        }

        try {
            params.accumulate("city_id",Integer.parseInt(sqlite.getCiudad().get(0).get_id()));
            params.accumulate("subcategories",sb_id);
            params.accumulate("name",name.getText().toString());

            load(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
