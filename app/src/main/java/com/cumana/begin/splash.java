package com.cumana.begin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
            //donwloadInfo();
            String response = "{'city':{'_id':'cum','name':'Cumaná','history':'<p>Cumaná es una importante ciudad del oriente venezolano, capital y sede de los poderes públicos del estado Sucre, Venezuela.</p> <p>Está ubicada en la entrada del golfo de Cariaco, junto a la desembocadura del río Manzanares. Actualmente posee una población de 374.706 habitantes, a los que hay que sumarle los de las localidades aledañas de El Peñón, Cantarrana y El Tacal con las que ha formado una conurbación con un total de 480.918 habitantes formando el Área Metropolitana de Cumaná, distribuidos en 598 km² de superficie; pertenece a la Región Nor-Oriental del país.<p>\n" +
                    "\n" +
                    "Cumaná fue la primera ciudad fundada por europeos en Tierra Firme del continente americano, el 27 de noviembre de 1515, aunque ya desde años antes se la conocía como Puerto de las Perlas, por lo que es conocida como La Primogénita del continente americano capital del Estado Sucre y centro de la región oriental. Desde el 3 de julio de 1591 recibe expresamente título de ciudad, con privilegio de escudo de armas. Desde entonces obtiene su patente histórica para no perder más el nombre, y ahí está, con su hermosa carga a cuestas. <p>Actualmente es fuerte candidata para ser nombrada patrimonio de la humanidad por la UNESCO, específicamente su casco histórico.</p>','woeid':'468383','__v':0,'salientPeople':[{'name':'Antonio José de Sucre','photo':'http://www.vtv.gob.ve/articulos/2013/06/03/antonio-jose-de-sucre-gran-mariscal-de-ayacucho-gran-mariscal-de-nuestramerica-y-de-la-libertad-8154.html/antonio-jose-de-sucre.jpeg/@@images/e690daa5-8cc5-48ad-9aa4-9fd1b7adfd6b.jpeg','profession':'Militar','resume':'Antonio José de Sucre fue uno de los héroes de la independencia latinoamericana más laureados y admirados.','description':'<p>Conocido como El Gran Mariscal de Ayacucho, fue un político, diplomático, estadista y militar venezolano, prócer de la independencia americana, así como presidente de Bolivia, Gobernador del Perú, General en Jefe del Ejército de la Gran Colombia, Comandante del Ejército del Sur y Gran Mariscal de Ayacucho.</p><p>Era hijo de una familia acomodada de tradición militar, siendo su padre coronel del Ejército Patriota. Es considerado como uno de los militares más completos entre los próceres de la independencia sudamericana.</p>','_id':'56273052526c62641056b556'},{'name':'Andrés Eloy Blanco','photo':'http://www.monografias.com/trabajos94/poemas-andres-eloy-blanco/image002.jpg','profession':'Escritor/Poeta','resume':'Importante poeta venezolano, miembro de la \"Generación del 28\" y fundador del Partido Acción Democrática (AD).','description':'<p>Importante poeta venezolano, miembro de la \"Generación del 28\" y fundador del Partido Acción Democrática (AD). Fue también cuentista, dramaturgo, periodista, biógrafo, ensayista e insigne orador.</p><p>Obtuvo inmuerables galardones por sus obras literarias alcanzando el éxito tanto dentro de sus fronteras como afuera. Funcionario público durante el gobierno de López Contreras, fue diputado y posteriormente presidente de la Asamblea Constituyente de 1946, en la cual se destacó por sus famosos discursos. Ejerció también el ministerio de Relaciones Exteriores durante el gobierno de Gallegos.</p>','_id':'56273052526c62641056b555'}],'multimedia':[{'type':'photo','url':'http://www.google.co.ve/url?sa=i&source=imgres&cd=&ved=0CAYQjBwwAGoVChMIsL2htpbYyAIVwZMeCh3wRwb8&url=http%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fd%2Fd5%2FIglesia_Catedral_de_Cuman%25C3%25A1%2C_Venezuela.jpg&psig=AFQjCNGqxFz8-bS-MCByiQUR7yK16LEflg&ust=1445675332065845','show':'true'},{'type':'photo','url':'http://www.venezuelatuya.com/oriente/cumana/catedral_de_cumana_gde.jpg','show':'false'},{'type':'video','url':'www.youtube.com/video1','show':'true'},{'type':'video','url':'www.youtube.com/video1','show':'false'}]},'wheather':[{'code':'4','date':'22 Oct 2015','day':'Thu','high':'93','low':'76','text':'Thunderstorms'},{'code':'4','date':'23 Oct 2015','day':'Fri','high':'95','low':'76','text':'Thunderstorms'},{'code':'38','date':'24 Oct 2015','day':'Sat','high':'94','low':'76','text':'Scattered Thunderstorms'},{'code':'38','date':'25 Oct 2015','day':'Sun','high':'93','low':'76','text':'PM Thunderstorms'},{'code':'30','date':'26 Oct 2015','day':'Mon','high':'95','low':'75','text':'Partly Cloudy'}],'categories':[{'category':{'_id':'turismo','name':'Turismo','icon':'http://146.148.119.202/resources/nearby/icon/icon_places.png','__v':0},'subcategory':[{'_id':'hotelesyposadas','name':'Hoteles y Posadas','idCat':'turismo','icon':'http://146.148.119.202/resources/nearby/marker/cafeteria.png','__v':0},{'_id':'restaurantes','name':'Restaurantes','idCat':'turismo','icon':'http://146.148.119.202/resources/nearby/marker/cafeteria.png','__v':0},{'_id':'centrocomercial','name':'Centros Comerciales','idCat':'turismo','icon':'http://146.148.119.202/resources/nearby/marker/cafeteria.png','__v':0},{'_id':'atraccionesdeportivas','name':'Atracciones Deportivas','idCat':'turismo','icon':'http://146.148.119.202/resources/nearby/marker/deporte.png','__v':0},{'_id':'playas','name':'Playas','idCat':'turismo','icon':'http://146.148.119.202/resources/nearby/marker/deporte.png','__v':0}]},{'category':{'_id':'movilidad','name':'Movilidad','icon':'http://146.148.119.202/resources/nearby/icon/icon_mobilidad.png','__v':0},'subcategory':[{'_id':'aeropuertos','name':'Aeropuertos','idCat':'movilidad','icon':'http://146.148.119.202/resources/nearby/marker/airport.png','__v':0},{'_id':'paradastaxi','name':'Paradas de Taxi','idCat':'movilidad','icon':'http://146.148.119.202/resources/nearby/marker/airport.png','__v':0},{'_id':'terminales','name':'Terminales','idCat':'movilidad','icon':'http://146.148.119.202/resources/nearby/marker/airport.png','__v':0}]},{'category':{'_id':'institucion','name':'Institución','icon':'http://146.148.119.202/resources/nearby/icon/icon_architecture.png','__v':0},'subcategory':[{'_id':'universidades','name':'Universidades','idCat':'institucion','icon':'http://146.148.119.202/resources/nearby/marker/museo.png','__v':0},{'_id':'salud','name':'Salud','idCat':'institucion','icon':'http://146.148.119.202/resources/nearby/marker/museo.png','__v':0},{'_id':'seguridad','name':'Seguridad','idCat':'institucion','icon':'http://146.148.119.202/resources/nearby/marker/museo.png','__v':0},{'_id':'infocentro','name':'Infocentro','idCat':'institucion','icon':'http://146.148.119.202/resources/nearby/marker/museo.png','__v':0},{'_id':'cyt','name':'Ciencias y Tecnología','idCat':'institucion','icon':'http://146.148.119.202/resources/nearby/marker/museo.png','__v':0},{'_id':'ejecutivo','name':'Ejecutivo Regional','idCat':'institucion','icon':'http://146.148.119.202/resources/nearby/marker/museo.png','__v':0}]}]}";

            try {
                process(new JSONObject(response));
            }catch (JSONException e){
                e.printStackTrace();
            }

        }else{
            TIME_OUT = 100;
            new moveProgress().execute();
        }
    }

    public void donwloadInfo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new utils().url+"init/cum";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            json = new JSONObject(response);

                            //process("");

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
                    donwloadImg(1, CityGalery.getJSONObject(i).getString("url"), CityGalery.getJSONObject(i).getString("url"), CityGalery.getJSONObject(i).getString("type"));
                }
            }


            //descargamos las imagenes de las caterogias y subcategory
            for(int i=0;i<JsonCategory.length();i++){
                total++;
                donwloadImg(4,JsonCategory.getJSONObject(i).getJSONObject("category").getString("icon"),JsonCategory.getJSONObject(i).getJSONObject("category").getString("_id"),null);

                JSONArray subcategory = JsonCategory.getJSONObject(i).getJSONArray("subcategory");
                for(int j=0;j<subcategory.length();j++){
                    total++;
                    donwloadImg(4,subcategory.getJSONObject(i).getString("icon"),subcategory.getJSONObject(i).getString("_id"),null);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void donwloadImg(final int table,String url,final String _id,final String type){

        RequestQueue queue = Volley.newRequestQueue(this);

        // Petición para obtener la imagen
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    public void onResponse(Bitmap bitmap) {
                        Log.w("Imagen", table+"" + bitmap);

                        if(table==2){
                           sqlite.add(table,new table_img(_id,new converteBitmap().BitMapToString(bitmap)).setImage());
                        }else{
                            if(table == 1){
                                sqlite.add(table,new table_img(_id,new converteBitmap().BitMapToString(bitmap),type).setImageType());
                            }else{
                                sqlite.add(table,new table_img(_id,new converteBitmap().BitMapToString(bitmap)).setImage());
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
                        Log.w("Imagen",""+error);
                    }
                });

        // Añadir petición a la cola
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
