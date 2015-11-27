package com.cumana.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cumana.struct.temp;
import com.cumana.tables.ciudad;
import com.cumana.tables.clima;
import com.cumana.tables.table_img;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier on 22-10-2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper mInstance = null;

    //Datos generales de la BD
    public static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "cumana";

    public static Context context;

    private static final String TABLE_CIUDAD = "ciudad";
    private static final String TABLE_CIUDAD_ID = "_id";
    private static final String TABLE_CIUDAD_NAME = "nombre";
    private static final String TABLE_CIUDAD_HISTORY = "historia";
    private static final String TABLE_CIUDAD_PERSONAJES = "personajes";

    private static final String TABLE_IMG_CITY = "ciudad_img";
    private static final String TABLE_IMG_CITY_ID ="_id";
    private static final String TABLE_IMG_CITY_CONTENT = "image";
    private static final String TABLE_IMG_CITY_TYPE = "tipo";

    private static final String TABLE_PERSONAJES = "personajes";
    private static final String TABLE_PERSONAJES_ID = "_id";
    private static final String TABLE_PERSONAJES_IMAGE = "image";

    private static final String TABLE_CATEGORY = "categorias";

    private static final String TABLE_CATEGORY_IMG = "categorias_img";

    private static final String TABLE_SUBCATEGORY_IMG = "subcategorias_img";

    private static final String TABLE_CLIMA = "clima";

    private static final String TABLE_TEMP = "temp";
    private static final String TABLE_TEMP_URL = "url";
    private static final String TABLE_TEMP_TABLE = "tabla";
    private static final String TABLE_TEMP_TYPE = "type";
    private static final String TABLE_TEMP_ID = "_id";

    public static String[] TABLA_BD = {"ciudad","ciudad_img","personajes","categorias","categorias_img","clima","subcategorias_img","temp"};

    final public static synchronized SQLiteHelper getHelper(Context ctx){

        if(mInstance == null){
            mInstance = new SQLiteHelper(ctx.getApplicationContext());
        }

        context = ctx;

        return mInstance;
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        String sql="";

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_CIUDAD+" ("+TABLE_CIUDAD_ID+" TEXT PRIMARY KEY,"+TABLE_CIUDAD_NAME+" TEXT,"+TABLE_CIUDAD_HISTORY+" TEXT,"+TABLE_CIUDAD_PERSONAJES+" TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_IMG_CITY+" ("+TABLE_IMG_CITY_ID+" TEXT PRIMARY KEY,"+TABLE_IMG_CITY_CONTENT+" TEXT,"+TABLE_IMG_CITY_TYPE+" TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_PERSONAJES+" ("+TABLE_PERSONAJES_ID+" TEXT PRIMARY KEY,"+TABLE_PERSONAJES_IMAGE+" TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_CATEGORY+" ("+TABLE_CATEGORY+" TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_CATEGORY_IMG+" (_id TEXT,image TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_SUBCATEGORY_IMG+" (_id TEXT,image TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_CLIMA+" ("+TABLE_CLIMA+" TEXT)";
        db.execSQL(sql);


        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_TEMP+" ("+TABLE_TEMP_ID+" TEXT,"+TABLE_TEMP_URL+" TEXT,"+TABLE_TEMP_TABLE+" TEXT,"+TABLE_TEMP_TYPE+" TEXT)";
        db.execSQL(sql);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //actualizacion de la BD
    }


    public long add(int table,ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            return db.insert(TABLA_BD[table], null, values);
        }catch(Exception e){
            return -1;
        }
    }

    public long remove(int table,String key,String comparation){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLA_BD[table],key+" = ?",new String[] { String.valueOf(comparation)});
    }

    public void removeAllBd(){
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i=0;i<TABLA_BD.length;i++){
            db.execSQL("DELETE FROM "+TABLA_BD[i]+"");
        }
    }

    public List<ciudad> getCiudad() {

        List<ciudad> queryCiudad = new ArrayList<ciudad>();

        String query = "SELECT * FROM " + TABLA_BD[0];

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ciudad cd = null;
        if (cursor.moveToFirst()) {
            do {
                cd = new ciudad();
                cd.set_id(cursor.getString(0));
                cd.setName(cursor.getString(1));
                cd.setHistory(cursor.getString(2));
                cd.setPersonajes(cursor.getString(3));

                queryCiudad.add(cd);
            } while (cursor.moveToNext());
        }

        return queryCiudad;
    }

    public List<table_img> getPersons(String _id){

        List<table_img> queryPersons = new ArrayList<table_img>();

        String query = "SELECT * FROM " + TABLA_BD[2]+" WHERE _id = '"+_id+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        table_img cl = null;
        if (cursor.moveToFirst()) {
            do {
                cl = new table_img(cursor.getString(0),cursor.getString(1));
                queryPersons.add(cl);

            } while (cursor.moveToNext());
        }

        return queryPersons;

    }

    public List<clima> getClima(){

        List<clima> queryClima = new ArrayList<clima>();

        String query = "SELECT * FROM " + TABLA_BD[5];

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        clima cl = null;
        if (cursor.moveToFirst()) {
            do {
                cl = new clima(cursor.getString(0));
                queryClima.add(cl);

            } while (cursor.moveToNext());
        }

        return queryClima;
    }

    public List<clima> getCategory(){

        List<clima> queryClima = new ArrayList<clima>();

        String query = "SELECT * FROM " + TABLA_BD[3];

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        clima cl = null;
        if (cursor.moveToFirst()) {
            do {
                cl = new clima(cursor.getString(0));
                queryClima.add(cl);

            } while (cursor.moveToNext());
        }

        return queryClima;
    }


    public List<table_img> getcategoryImg(int table,String id){

        List<table_img> querySubcat = new ArrayList<table_img>();

        String query = "SELECT * FROM " + TABLA_BD[table]+" WHERE _id = '"+id+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        table_img cl = null;
        if (cursor.moveToFirst()) {
            do {
                cl = new table_img();
                cl.set_id(cursor.getString(0));
                cl.setImg(cursor.getString(1));
                querySubcat.add(cl);

            } while (cursor.moveToNext());
        }

        return querySubcat;
    }


    public List<table_img> getcategoryImg(int table){

        List<table_img> querySubcat = new ArrayList<table_img>();

        String query = "SELECT * FROM " + TABLA_BD[table]+"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        table_img cl = null;
        if (cursor.moveToFirst()) {
            do {
                cl = new table_img();
                cl.set_id(cursor.getString(0));
                cl.setImg(cursor.getString(1));
                querySubcat.add(cl);

            } while (cursor.moveToNext());
        }

        return querySubcat;
    }


    public List<temp> temps(){

        List<temp> querySubcat = new ArrayList<temp>();

        String query = "SELECT * FROM " + TABLA_BD[7]+"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        temp cl = null;
        if (cursor.moveToFirst()) {
            do {
                cl = new temp();
                cl.set_id(cursor.getString(0));
                cl.setUrl(cursor.getString(1));
                cl.setTable(cursor.getInt(2));
                cl.setType(cursor.getString(3));

                querySubcat.add(cl);

            } while (cursor.moveToNext());
        }

        return querySubcat;
    }
}
