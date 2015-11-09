package com.cumana.history;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.widget.ImageView;

import com.cumana.bitmap.converteBitmap;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.tables.ciudad;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier on 02-11-2015.
 */
public class details_persons extends AppCompatActivity{

    SQLiteHelper sqlite;
    JSONArray persons;
    List<ciudad> queryCiudad = new ArrayList<>();
    ImageView image,fondo;
    TextView history;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_persons);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        image = (ImageView) findViewById(R.id.image);
        fondo = (ImageView) findViewById(R.id.fondo);
        history = (TextView) findViewById(R.id.historia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapser);

        sqlite = SQLiteHelper.getHelper(this);

        queryCiudad = sqlite.getCiudad();

        try {
            persons = new JSONArray(queryCiudad.get(0).getPersonajes());
            image.setImageBitmap(new converteBitmap().StringToBitMap(sqlite.getPersons(persons.getJSONObject(Integer.parseInt("" + getIntent().getExtras().getInt("details_id"))).getString("_id")).get(0).getImg()));
            fondo.setImageBitmap(new converteBitmap().StringToBitMap(sqlite.getPersons(persons.getJSONObject(Integer.parseInt("" + getIntent().getExtras().getInt("details_id"))).getString("_id")).get(0).getImg()));
            history.setText(Html.fromHtml(persons.getJSONObject(Integer.parseInt("" + getIntent().getExtras().getInt("details_id"))).getString("description")));
            collapsingToolbar.setTitle(persons.getJSONObject(Integer.parseInt("" + getIntent().getExtras().getInt("details_id"))).getString("name"));

            collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

            BitmapDrawable drawable = (BitmapDrawable) fondo.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Bitmap blurred = blurRenderScript(bitmap, 25);
            fondo.setImageBitmap(blurred);
        }catch (JSONException e){
            e.printStackTrace();
        }

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

}
