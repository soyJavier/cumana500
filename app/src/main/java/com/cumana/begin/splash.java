package com.cumana.begin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cumana.bitmap.converteBitmap;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import java.util.ArrayList;
import com.cumana.menu.menu;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.tables.ciudad;
import com.cumana.tables.clima;
import com.cumana.tables.table_img;
import com.cumana.utils.utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Javier on 19-10-2015.
 */
public class splash extends AppCompatActivity {

    private ImageSwitcher fons_slider;
    private TextView place;
    private ArrayList<Integer> Resource = new ArrayList<Integer>(){{add(R.mipmap.intro_1);add(R.mipmap.intro_2);add(R.mipmap.intro_3);}};
    private ArrayList<Integer> ResourceSite = new ArrayList<Integer>(){{add(R.string.castillo_san_antonio);add(R.string.monumento_indio);add(R.string.iglesia_ines);}};

    private ProgressBar loading;
    private TextView percentage;

    JSONObject json;

    SQLiteHelper sqlite;

    SharedPreferences optionsMenu;

    int TIME_OUT = 250;

    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        sqlite = SQLiteHelper.getHelper(this);

        optionsMenu = getSharedPreferences(splash.class.getSimpleName(), Context.MODE_PRIVATE);
        optionsMenu.getBoolean("active", false);
        optionsMenu.getBoolean("wheather", false);

        fons_slider = (ImageSwitcher) findViewById(R.id.fonds_search);
        place = (TextView) findViewById(R.id.place);
        loading = (ProgressBar) findViewById(R.id.loading);
        percentage = (TextView) findViewById(R.id.percentage);

        fons_slider.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
                return myView;
            }
        });

        fons_slider.setImageResource(Resource.get(0));
        place.setText(ResourceSite.get(0));

        if(!optionsMenu.getBoolean("active",false)){
            donwloadInfo();
        }else{
            TIME_OUT = 100;
            new moveProgress().execute();
        }
    }

    public void donwloadInfo(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = new utils().url+"init/download/10.468400048880145/-66.89036584999997";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            json = new JSONObject(response);
                            process(json);

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("console", "" + error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                16000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void process(JSONObject data){

        Log.w("data",""+data);
        try{

            //Json Ciudad
            JSONObject JsonCity = data.getJSONObject("city");
            JSONArray CityPersons = JsonCity.getJSONArray("salientPeople");
            JSONArray CityGalery = JsonCity.getJSONArray("multimedia");

            //Json category
            JSONArray JsonCategory = data.getJSONArray("categories");

            //Insertamos datos del clima en la bd
            sqlite.add(5,new clima(data.getString("wheather")).setClima());

            //Insertamos datos de las categorias y subcategorias en la bd
            sqlite.add(3, new clima(data.getString("categories")).setCategory());

            ciudad city = new ciudad();
            city.set_id(JsonCity.getString("_id"));
            city.setName(JsonCity.getString("name"));
            city.setHistory(JsonCity.getString("history"));
            city.setPersonajes(JsonCity.getString("salientPeople"));

            //insertamos la informacion de la ciudad
            sqlite.add(0,city.setCiudad());

            //descarga de las imagenes de los personajes emblematicos
            for(int i=0;i<CityPersons.length();i++){
                total++;
                donwloadImg(2,CityPersons.getJSONObject(i).getString("photo"),CityPersons.getJSONObject(i).getString("_id"),null);
            }

            //descarga de las imagenes de la multimedia
            for(int i=0;i<CityGalery.length();i++){
                if(!CityGalery.getJSONObject(i).getString("type").equals("video")) {
                    total++;
                    donwloadImg(1,CityGalery.getJSONObject(i).getString("photo"), CityGalery.getJSONObject(i).getString("photo"), CityGalery.getJSONObject(i).getString("type"));
                }
            }

            //descargamos las imagenes de las caterogias y subcategory
            for(int i=0;i<JsonCategory.length();i++){
                total++;
                donwloadImg(4,JsonCategory.getJSONObject(i).getString("icon"),JsonCategory.getJSONObject(i).getString("id_category"),null);

                JSONArray subcategory = JsonCategory.getJSONObject(i).getJSONArray("subcategory");
                for(int j=0;j<subcategory.length();j++){
                    total++;
                    donwloadImg(6,subcategory.getJSONObject(j).getString("icon"),subcategory.getJSONObject(j).getString("id_subcategory"),null);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void donwloadImg(int tb,String u,String id,String ty){

        final int table = tb;
        final String url = u;
        final String _id = id;
        final String type = ty;

        RequestQueue queue = Volley.newRequestQueue(this);

        // Petición para obtener la imagen
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    public void onResponse(Bitmap bitmap) {

                        if(table==2){
                           sqlite.add(table,new table_img(_id,new converteBitmap().BitMapToString(bitmap)).setImage());
                        }else{
                            if(table == 1){
                                sqlite.add(table,new table_img(_id,new converteBitmap().BitMapToString(bitmap),type).setImageType());
                            }else{
                                sqlite.add(table,new table_img(_id,new converteBitmap().BitMapToString(bitmap)).setImage());

                                Log.w("tabla",""+table+" "+_id+" "+bitmap);
                            }
                        }

                        total--;

                        Log.w("total",""+total);
                        if(total == 1){
                            new moveProgress().execute();

                            SharedPreferences.Editor editor = optionsMenu.edit();

                            editor.putBoolean("active",true);
                            editor.commit();

                        }else{
                            loading.setProgress(loading.getProgress() + 1);
                            percentage.setText(""+loading.getProgress()+"%");
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.w("error",""+url+" error");
                        if(!isFinishing()) {
                            donwloadImg(table, url, _id, type);
                        }
                    }
                });

        // Añadir petición a la cola
        request.setRetryPolicy(new DefaultRetryPolicy(
                16000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public class moveProgress extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(TIME_OUT);//300
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if((loading.getProgress()+1)<=100) {

                loading.setProgress(loading.getProgress()+1);

                percentage.setText(""+loading.getProgress()+"%");
                if(loading.getProgress() == 30) {
                    Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                    Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    fons_slider.setInAnimation(fadeIn);
                    fons_slider.setOutAnimation(fadeOut);
                    fons_slider.setImageResource(Resource.get(1));
                    place.setText(ResourceSite.get(1));

                }else{
                    if(loading.getProgress() == 60){
                        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                        fons_slider.setInAnimation(fadeIn);
                        fons_slider.setOutAnimation(fadeOut);
                        fons_slider.setImageResource(Resource.get(2));
                        place.setText(ResourceSite.get(2));
                    }
                }
                new moveProgress().execute();
            }else{
                startActivity(new Intent().setClass(getApplicationContext(),menu.class));
                finish();
            }
            super.onPostExecute(aVoid);
        }
    }

}
