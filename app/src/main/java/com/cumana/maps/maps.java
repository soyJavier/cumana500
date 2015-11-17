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
import android.view.View;

import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class maps extends AppCompatActivity {

    ActionBar actionBar;
    Toolbar toolbar;

    TextView title;

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
        actionBar.setDisplayShowTitleEnabled(true);

        title = (TextView) toolbar.findViewById(R.id.title);

        Bundle params = new Bundle();

        if(getIntent().getExtras().containsKey("single")){
            params.putBoolean("single", true);
            params.putString("details", "" + getIntent().getExtras().getString("details"));

            try {
                JSONObject place = new JSONObject(getIntent().getExtras().getString("details"));
                title.setText(place.getString("name"));
                title.setVisibility(View.VISIBLE);
                actionBar.setTitle(place.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.icon_menu, menu);

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
