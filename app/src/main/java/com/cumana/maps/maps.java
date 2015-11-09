package com.cumana.maps;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.cumana.cumana500.R;


public class maps extends AppCompatActivity {

    ActionBar actionBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        Bundle params = new Bundle();

        if(getIntent().getExtras().containsKey("single")){
            Log.w("single", "single");
        }else{
            params.putString("json","" +getIntent().getExtras().getString("json"));
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        f_maps mp = new f_maps();
        mp.setArguments(params);
        fragmentTransaction.replace(R.id.map,mp);
        fragmentTransaction.commit();

        /*mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.maps)).getMap();

        if(getIntent().getExtras().containsKey("single")){
            Log.w("single","single");
        }else{
            try {
                points = new JSONArray(getIntent().getExtras().getString("json"));


                for (int i=0;i<points.length();i++){
                    builder.include(new LatLng(points.getJSONObject(i).getDouble("lat"), points.getJSONObject(i).getDouble("lon")));
                    MarkerOptions m = new MarkerOptions();
                    m.position(new LatLng(points.getJSONObject(i).getDouble("lat"), points.getJSONObject(i).getDouble("lon")));

                    mapa.addMarker(m);
                }


                //mapa.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), displaymetrics.widthPixels, displaymetrics.heightPixels, (int) (Math.min(100,100) * 0.1)));

                Log.w("json",points.toString());
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                onBackPressed();
                break;
            case R.id.search:{
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
