package com.cumana.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.cumana.adapters.adapter_services;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.maps.maps;
import com.cumana.struct.services;
import com.cumana.utils.PlaceData;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Javier on 21-10-2015.
 */
public class details extends AppCompatActivity {

    JSONObject json;
    TextView details,address,name_places,contac;
    ImageView image;
    LinearLayout cService;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager lManager;
    private LinearLayout location,ContentContac;
    List items = new ArrayList();

    ArrayList<String> id = new ArrayList<String>(){{add("handicap");add("kidsfamily");add("ac");add("wifi");add("nightlife");add("pet");add("gayfriendly");add("free");add("walking");add("gym");add("parking");add("pool");add("restaurant");add("suites");add("Conference room");add("Live music");add("Lavandería");add("Bar");add("Vacation Club");}};
    ArrayList<String> spain = new ArrayList<String>(){{add("Discapacitado");add("Familiar");add("A/C");add("Wifi");add("Vida nocturna");add("Mascotas");add("De ambiente");add("Gratis");add("Caminata");add("GYM");add("Estacionamiento");add("Piscina");add("Restaurante");add("Suites");add("Conferencias");add("Musica en vivo");add("Lavandería");add("Bar");add("Club vacacional");}};
    ArrayList<Integer> icon = new ArrayList<Integer>(){{add(R.mipmap.ic_handicap);add(R.mipmap.ic_kidsfamily);add(R.mipmap.ic_air);add(R.mipmap.ic_wifi);add(R.mipmap.ic_nightlife);add(R.mipmap.ic_pet);add(R.mipmap.ic_gayfriendly);add(R.mipmap.ic_free);add(R.mipmap.ic_walking);add(R.mipmap.ic_gym);add(R.mipmap.ic_parking);add(R.mipmap.ic_pool);add(R.mipmap.ic_restaurant);add(R.mipmap.ic_suite);add(R.mipmap.ic_conference);add(R.mipmap.ic_music);add(R.mipmap.ic_washing);add(R.mipmap.ic_bar);add(R.mipmap.ic_vacation);}};

    private SliderLayout mDemoSlider;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        image = (ImageView) findViewById(R.id.image);
        address = (TextView) findViewById(R.id.address);
        details = (TextView) findViewById(R.id.details);
        location = (LinearLayout) findViewById(R.id.location);
        ContentContac = (LinearLayout) findViewById(R.id.ContentContac);
        name_places = (TextView) findViewById(R.id.name_places);
        contac = (TextView) findViewById(R.id.contac);
        cService = (LinearLayout) findViewById(R.id.cService);
        recycler = (RecyclerView) findViewById(R.id.servicios);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(lManager);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapser);

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBarNull);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.ExpandedAppBarNull);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBarNull);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.ExpandedAppBar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image.setImageBitmap(new PlaceData().listPlaces.get(getIntent().getExtras().getInt("position")).getBitmap());

        try {
            json = new JSONObject(getIntent().getExtras().getString("details"));

            collapsingToolbar.setTitle(json.getString("name"));
            name_places.setText(json.getString("name"));

            address.setText(json.getString("address"));

            JSONArray Jservices = json.getJSONArray("services");

            for(int i=0;i<Jservices.length();i++){
                items.add(new services(icon.get(id.indexOf(Jservices.getJSONObject(i).getString("name"))),spain.get(id.indexOf(Jservices.getJSONObject(i).getString("name")))));
            }

            if(Jservices.length()>0){
                cService.setVisibility(View.VISIBLE);
            }

            adapter = new adapter_services(items);
            recycler.setAdapter(adapter);

            if(json.getString("description").length()>0) {
                details.setText(Html.fromHtml(json.getString("description")));
                details.setVisibility(View.VISIBLE);
            }

            if(json.getJSONArray("images").length()>1){

                JSONArray images = json.getJSONArray("images");

                mDemoSlider = (SliderLayout) findViewById(R.id.slider);

                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(1000);

                for(int i=0;i<images.length();i++){

                    TextSliderView textSliderView = new TextSliderView(this);
                    textSliderView
                            .image(images.getString(i))
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop);

                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",images.getString(i));

                    mDemoSlider.addSlider(textSliderView);
                }

                mDemoSlider.stopAutoCycle();
                mDemoSlider.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
            }

            if(json.getJSONArray("phone").length()>0){

                JSONArray phone = json.getJSONArray("phone");
                String ctc = "";
                for(int i=0;i<phone.length();i++){
                    ctc += ""+phone.getString(i)+"<br>";
                }
                contac.setText(Html.fromHtml(ctc));
                ContentContac.setVisibility(View.VISIBLE);
            }

            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent move = new Intent(details.this, maps.class);
                    move.putExtra("single","true");
                    move.putExtra("details",getIntent().getExtras().getString("details"));

                    Pair<View, String> holderPair = Pair.create((View) location, "tDirection");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(details.this,holderPair);
                    ActivityCompat.startActivity(details.this, move, options.toBundle());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.icon_menu, menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
