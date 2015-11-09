package com.cumana.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cumana.begin.splash;
import com.cumana.cumana500.R;
import com.cumana.developers.developers;
import com.cumana.fonts.TextView;
import com.cumana.history.history;
import com.cumana.list.list;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.tables.clima;
import com.cumana.utils.Connectivity;
import com.cumana.utils.utils;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Javier on 20-10-2015.
 */
public class menu extends AppCompatActivity implements View.OnClickListener{

    ImageView fondo,logo;
    Toolbar toolbar;
    ActionBar actionBar;
    LinearLayout institucion,historia,turismo,descargas;
    SQLiteHelper sqlite;
    SharedPreferences optionsMenu;

    List<Integer> ids = new ArrayList<Integer>(){{add(R.id.today);add(R.id.day_1);add(R.id.day_2);add(R.id.day_3);add(R.id.day_4);}};
    List<Integer> ids_day = new ArrayList<Integer>(){{add(R.id.today);add(R.id.text_day_1);add(R.id.text_day_2);add(R.id.text_day_3);add(R.id.text_day_4);}};

    List<Integer> ResourceClima = new ArrayList<Integer>(){{add(R.mipmap.lluviatruenos);add(R.mipmap.nublado_truenos);add(R.mipmap.nublado_sol);add(R.mipmap.nublado_luna);}};
    List<String> codeClima = new ArrayList<String>(){{add("4");add("38");add("30");add("29");}};

    //data
    JSONArray clima;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        sqlite = SQLiteHelper.getHelper(this);
        optionsMenu = getSharedPreferences(splash.class.getSimpleName(), Context.MODE_PRIVATE);

        fondo = (ImageView) findViewById(R.id.fondo);
        institucion = (LinearLayout) findViewById(R.id.institucion);
        historia = (LinearLayout) findViewById(R.id.historia);
        turismo = (LinearLayout) findViewById(R.id.turismo);
        descargas = (LinearLayout) findViewById(R.id.descargas);

        BitmapDrawable drawable = (BitmapDrawable) fondo.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap blurred = blurRenderScript(bitmap, 5);
        fondo.setImageBitmap(blurred);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);

        logo = (ImageView) toolbar.findViewById(R.id.logo);

        logo.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        actionBar.setHomeAsUpIndicator(R.mipmap.ic_crown);

        institucion.setOnClickListener(this);
        turismo.setOnClickListener(this);
        historia.setOnClickListener(this);

        if(!optionsMenu.getBoolean("wheather", false)){

            SharedPreferences.Editor edit = optionsMenu.edit();
            edit.putBoolean("wheather",true);
            edit.commit();

            setWheather();

        }else{
            if(new Connectivity(this).isConnecting()){
                donwloadInfo();
            }else{
                setWheather();
            }
        }
    }

    public void donwloadInfo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new utils().url+"wheather/cum";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            clima = new JSONArray(response);
                            Log.w("response",response);
                            process(clima);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setWheather();
                Log.w("console", "" + error);
            }
        });
        queue.add(stringRequest);
    }

    private void process(JSONArray clima) {
        //Insertamos datos del clima en la bd
        sqlite.add(5,new clima(clima.toString()).setClima());

        setWheather();
    }

    @Override
    protected void onResume() {
        if(sqlite.getClima().size()>0) {
            setWheather();
        }
        super.onResume();
    }

    public void setWheather(){
        try {
            clima = new JSONArray(sqlite.getClima().get(0).getContenido());

            TextView today = (TextView) findViewById(ids_day.get(0));

            today.setText(((clima.getJSONObject(0).getInt("high") - 32) * 5 / 9) + "°");

            for (int i = 1; i < clima.length(); i++) {
                ImageView img = (ImageView) findViewById(ids.get(i));
                TextView day = (TextView) findViewById(ids_day.get(i));

                if (codeClima.contains(clima.getJSONObject(i).getString("code"))) {
                    img.setImageResource(ResourceClima.get(codeClima.indexOf(clima.getJSONObject(i).getString("code"))));
                    day.setText(clima.getJSONObject(i).getString("day"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.icon_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){


            case android.R.id.home:
                startActivity(new Intent().setClass(getApplicationContext(),developers.class));
                break;
            case R.id.search:{
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(getApplication());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    @Override
    public void onClick(View v) {

        Intent action = new Intent();

        switch (v.getId()){
            case R.id.institucion:
                action.putExtra("category", "institucion");
                action.putExtra("title","Instituciones");
                action.setClass(this, list.class);
                break;
            case R.id.turismo:
                action.putExtra("category", "turismo");
                action.putExtra("title","Turismo");
                action.setClass(this, list.class);
                break;
            case R.id.historia:
                action.setClass(this, history.class);
                break;
            case R.id.descargas:
                break;
        }

        startActivity(action);
    }
}
