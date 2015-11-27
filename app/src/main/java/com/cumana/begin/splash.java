package com.cumana.begin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cumana.menu.menu;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.struct.temp;
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
    private ArrayList<Integer> Resource = new ArrayList<Integer>(){{add(R.mipmap.intro_1);add(R.mipmap.intro_2);add(R.mipmap.intro_3);add(R.mipmap.intro_4);add(R.mipmap.intro_5);add(R.mipmap.intro_6);add(R.mipmap.intro_7);add(R.mipmap.intro_8);}};
    private ArrayList<Integer> ResourceSite = new ArrayList<Integer>(){{add(R.string.castillo_san_antonio);add(R.string.monumento_indio);add(R.string.iglesia_ines);add(R.string.calle_iglesia_ines);add(R.string.cumanagoto);add(R.string.castillo_san_antonio);add(R.string.iglesia_ines);add(R.string.calle_alacran);}};

    private ProgressBar loading;
    private TextView percentage;
    List<temp> temp = new ArrayList<temp>();
    List<Integer> listaNumero = new ArrayList<>();

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
        optionsMenu.getString("versionApps", "1.2");

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


        for(int i = 0; i < 4;i++){
            Log.w("generar",generar()+"");
        }

        if(!optionsMenu.getBoolean("active",false)){

            temp =  sqlite.temps();

            if(temp.size() == 0) {

                String vers = "";
                try {
                    vers = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    vers = "1.3";
                }

                if(optionsMenu.getString("versionApps","1.2").equals("1.2")){
                    sqlite.removeAllBd();
                    SharedPreferences.Editor editor = optionsMenu.edit();
                    editor.putString("versionApps",vers);
                    editor.commit();
                    donwloadInfo();
                }else{
                    TIME_OUT = 100;
                    new moveProgress().execute();
                }

            }else{
                donwloadInfoTemp();
            }
        }else{
            TIME_OUT = 100;
            new moveProgress().execute();
        }

        File file = new File(Environment.getExternalStorageDirectory(),"Cumana500");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.w("TravellerLog :: ","Problem creating Image folder");
            }else{
                Log.w("CRRETA","CREATE");
            }
        }
        file = new File(Environment.getExternalStorageDirectory(), "Cumana500/save");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.w("TravellerLog :: ","Problem creating Image folder");
            }else{
                Log.w("CRRETA","CREATE");
            }
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                Log.w("n","DENSITY_LOW");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.w("n","DENSITY_MEDIUM");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Log.w("n","DENSITY_HIGH");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Log.w("n","DENSITY_XHIGH");
                break;

            case DisplayMetrics.DENSITY_XXHIGH:
                Log.w("n","DENSITY_XXHIGH");
                break;

            case DisplayMetrics.DENSITY_XXXHIGH:
                Log.w("n","DENSITY_XXXHIGH");
                break;
        }
    }


    public int generar(){
        if(listaNumero.size() < (7 - 0) +1){
            //Aun no se han generado todos los numeros
            int numero = numeroAleatorio();//genero un numero
            if(listaNumero.isEmpty()){//si la lista esta vacia
                listaNumero.add(numero);
                return numero;
            }else{//si no esta vacia
                if(listaNumero.contains(numero)){//Si el numero que generé esta contenido en la lista
                    return generar();//recursivamente lo mando a generar otra vez
                }else{//Si no esta contenido en la lista
                    listaNumero.add(numero);
                    return numero;
                }
            }
        }else{// ya se generaron todos los numeros
            return -1;
        }
    }

    private int numeroAleatorio(){
        return (int)(Math.random()*(7-0+1)+0);
    }

    public void donwloadInfoTemp(){

        for(int i=0;i<temp.size();i++){
            total++;
            donwloadImg(temp.get(i).getTable(),temp.get(i).getUrl(),temp.get(i).get_id(),temp.get(i).getType());
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
                donwloadInfo();
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
                sqlite.add(7,new temp(CityPersons.getJSONObject(i).getString("_id"),2,CityPersons.getJSONObject(i).getString("photo"),null).setInfo());
            }

            //descarga de las imagenes de la multimedia
            for(int i=0;i<CityGalery.length();i++){
                if(!CityGalery.getJSONObject(i).getString("type").equals("video")) {
                    total++;
                    donwloadImg(1,CityGalery.getJSONObject(i).getString("photo"), CityGalery.getJSONObject(i).getString("photo"), CityGalery.getJSONObject(i).getString("type"));
                    sqlite.add(7,new temp(CityGalery.getJSONObject(i).getString("photo"),1,CityGalery.getJSONObject(i).getString("photo"),CityGalery.getJSONObject(i).getString("type")).setInfo());
                }
            }

            //descargamos las imagenes de las caterogias y subcategory
            for(int i=0;i<JsonCategory.length();i++){
                total++;
                donwloadImg(4,JsonCategory.getJSONObject(i).getString("icon"),JsonCategory.getJSONObject(i).getString("id_category"),null);
                sqlite.add(7,new temp(JsonCategory.getJSONObject(i).getString("id_category"),4,JsonCategory.getJSONObject(i).getString("icon"),null).setInfo());

                JSONArray subcategory = JsonCategory.getJSONObject(i).getJSONArray("subcategory");
                for(int j=0;j<subcategory.length();j++){
                    total++;
                    donwloadImg(6,subcategory.getJSONObject(j).getString("icon"),subcategory.getJSONObject(j).getString("id_subcategory"),null);
                    sqlite.add(7,new temp(subcategory.getJSONObject(j).getString("id_subcategory"),6,subcategory.getJSONObject(j).getString("icon"),null).setInfo());
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

                        long k = sqlite.remove(7,"_id",""+_id);

                        Log.w("remove",k+" - "+url);

                        Log.w("total",""+total);
                        if(total == 0 && sqlite.temps().size() == 0){
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
                if(loading.getProgress() == 25) {
                    Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                    Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    fons_slider.setInAnimation(fadeIn);
                    fons_slider.setOutAnimation(fadeOut);
                    fons_slider.setImageResource(Resource.get(listaNumero.get(0)));
                    place.setText(ResourceSite.get(listaNumero.get(0)));
                }else{
                    if(loading.getProgress() == 55){
                        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                        fons_slider.setInAnimation(fadeIn);
                        fons_slider.setOutAnimation(fadeOut);
                        fons_slider.setImageResource(Resource.get(listaNumero.get(1)));
                        place.setText(ResourceSite.get(listaNumero.get(1)));
                    }else{
                        if(loading.getProgress() == 70){
                            Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                            Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                            fons_slider.setInAnimation(fadeIn);
                            fons_slider.setOutAnimation(fadeOut);
                            fons_slider.setImageResource(Resource.get(listaNumero.get(2)));
                            place.setText(ResourceSite.get(listaNumero.get(2)));
                        }else{
                            if(loading.getProgress() == 90){
                                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                                Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                                fons_slider.setInAnimation(fadeIn);
                                fons_slider.setOutAnimation(fadeOut);
                                fons_slider.setImageResource(Resource.get(listaNumero.get(3)));
                                place.setText(ResourceSite.get(listaNumero.get(3)));
                            }
                        }
                    }
                }
                new moveProgress().execute();
            }else{
                startActivity(new Intent().setClass(getApplicationContext(), menu.class));
                finish();
            }
            super.onPostExecute(aVoid);
        }
    }

}
