package com.cumana.maps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.cumana.bitmap.converteBitmap;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.list.details;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.tables.table_img;
import com.cumana.utils.PlaceData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class f_maps extends Fragment implements GoogleMap.OnMarkerClickListener,View.OnClickListener{

    View view;
    JSONArray json;
    GoogleMap mapa;
    SQLiteHelper sqlite;
    DisplayMetrics displaymetrics = new DisplayMetrics();
    DisplayMetrics metrics;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    List<String> lat_ = new ArrayList<>();
    List<Integer> posi = new ArrayList<>();

    JSONObject single;

    TextView direction;

    /*Details places*/
    ImageView image,icon;
    CircleProgressBar loading;
    LinearLayout contentDirection;
    RelativeLayout places;
    TextView title,address,subcategory;
    int position = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.f_maps, container, false);

       metrics = new DisplayMetrics();
       getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
       getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
       sqlite = SQLiteHelper.getHelper(getActivity());

       direction = (TextView) view.findViewById(R.id.direction);

       DisplayMetrics metrics = new DisplayMetrics();
       getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

       setHasOptionsMenu(true);
       mapa =  ((SupportMapFragment) getMapFragment()).getMap();

        if(getArguments().containsKey("single")){

            try {
                single = new JSONObject(getArguments().getString("details"));
                direction.setText(single.getString("address"));

                Log.w("data_id",single.getString("id_subcategory")+"");

                List<table_img> img = sqlite.getcategoryImg(6,single.getString("id_subcategory"));

                MarkerOptions m = new MarkerOptions();
                m.position(new LatLng(single.getDouble("lat"), single.getDouble("lng")));


                m.icon(BitmapDescriptorFactory.fromBitmap(size(new converteBitmap().StringToBitMap(img.get(0).getImg()))));

                mapa.addMarker(m);
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(single.getDouble("lat"),single.getDouble("lng")), 15));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            contentDirection = (LinearLayout) view.findViewById(R.id.contentDirection);
            contentDirection.setVisibility(View.GONE);
            places = (RelativeLayout) view.findViewById(R.id.places);

            loading = (CircleProgressBar) view.findViewById(R.id.loading);
            image = (ImageView) view.findViewById(R.id.img);
            title = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.address);
            subcategory = (TextView) view.findViewById(R.id.subcategory);
            icon = (ImageView) view.findViewById(R.id.icon);
            places.setOnClickListener(this);

            mapa.setOnMarkerClickListener(this);
            mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (places.getVisibility() == View.VISIBLE) {
                        places.setVisibility(View.GONE);
                    }
                }
            });

            try {
                json = new JSONArray(getArguments().getString("json"));

                for(int i=0;i<json.length();i++){

                    builder.include(new LatLng(json.getJSONObject(i).getDouble("lat"), json.getJSONObject(i).getDouble("lng")));
                    MarkerOptions m = new MarkerOptions();
                    m.position(new LatLng(json.getJSONObject(i).getDouble("lat"), json.getJSONObject(i).getDouble("lng")));

                    List<table_img> img = sqlite.getcategoryImg(6,json.getJSONObject(i).getString("id_subcategory"));

                    lat_.add(i,""+json.getJSONObject(i).getDouble("lat"));

                    m.icon(BitmapDescriptorFactory.fromBitmap(size(new converteBitmap().StringToBitMap(img.get(0).getImg()))));

                    mapa.addMarker(m);
                }

                mapa.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), displaymetrics.widthPixels, displaymetrics.heightPixels, (int) (Math.min(view.getWidth(), view.getHeight()) * 0.1)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }


       return view;
    }

    public Bitmap size(Bitmap bitmap){

        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                Log.w("n","DENSITY_LOW");
                return Bitmap.createScaledBitmap(bitmap, 20, 29, false);
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.w("n","DENSITY_MEDIUM");
                return Bitmap.createScaledBitmap(bitmap, 20, 29, false);
            case DisplayMetrics.DENSITY_HIGH:
                Log.w("n","DENSITY_HIGH");
                return Bitmap.createScaledBitmap(bitmap, 28, 39, false);
            case DisplayMetrics.DENSITY_XHIGH:
                Log.w("n","DENSITY_XHIGH");
                return Bitmap.createScaledBitmap(bitmap, 44, 59, false);
            case DisplayMetrics.DENSITY_XXHIGH:
                Log.w("n","DENSITY_XXHIGH");
                return Bitmap.createScaledBitmap(bitmap, 79, 99, false);
            case DisplayMetrics.DENSITY_XXXHIGH:
                Log.w("n","DENSITY_XXXHIGH");
                return Bitmap.createScaledBitmap(bitmap, 79, 99, false);
            default:
                return Bitmap.createScaledBitmap(bitmap, 79, 99, false);
        }
    }

    private SupportMapFragment getMapFragment() {
        FragmentManager fm = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getChildFragmentManager();
        } else {
            fm = getFragmentManager();
        }

        return (SupportMapFragment) fm.findFragmentById(R.id.maps);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        double lat = marker.getPosition().latitude;
        final double l = marker.getPosition().latitude;
        position = lat_.indexOf(lat + "");

        try{
            title.setText(json.getJSONObject(position).getString("name"));
            subcategory.setText(json.getJSONObject(position).getString("subcategory"));
            address.setText(json.getJSONObject(position).getString("address"));
            icon.setImageBitmap(new converteBitmap().StringToBitMap(sqlite.getcategoryImg(6, json.getJSONObject(position).getString("id_subcategory")).get(0).getImg()));

            if(new PlaceData().listPlaces.size()>0) {
                if (new PlaceData().listPlaces.get(position).getBitmap() != null) {
                    image.setImageBitmap(new PlaceData().listPlaces.get(position).getBitmap());
                    loading.setVisibility(View.INVISIBLE);
                } else {
                    loading.setVisibility(View.VISIBLE);
                    image.setImageResource(android.R.color.white);
                    Picasso.with(getActivity()).load(json.getJSONObject(position).getJSONArray("images").getString(0)).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            image.setImageBitmap(bitmap);
                            new PlaceData().listPlaces.get(lat_.indexOf(l + "")).setBitmap(bitmap);
                            loading.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                }
            }else{
                loading.setVisibility(View.VISIBLE);
                image.setImageResource(android.R.color.white);
                Picasso.with(getActivity()).load(json.getJSONObject(position).getJSONArray("images").getString(0)).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        image.setImageBitmap(bitmap);
                        new PlaceData().listPlaces.get(lat_.indexOf(l + "")).setBitmap(bitmap);
                        loading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            places.setVisibility(View.VISIBLE);
        }catch(JSONException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onClick(View v) {

        try {

            Intent intent = new Intent(getActivity(), details.class);
            intent.putExtra("details", json.getString(position));
            intent.putExtra("position", position);

            Pair<View, String> imagePair = Pair.create((View) image, "tImage");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    imagePair);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
