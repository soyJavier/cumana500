package com.cumana.developers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.github.library.bubbleview.BubbleTextVew;

/**
 * Created by Javier on 21-10-2015.
 */
public class developers extends AppCompatActivity implements View.OnClickListener{

    ImageView fondo;
    Toolbar toolbar;
    ActionBar actionBar;
    BubbleTextVew description_1,description_2,description_3,description_4;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.developers);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        fondo = (ImageView) findViewById(R.id.fondo);

        description_1 = (BubbleTextVew) findViewById(R.id.description_1);
        description_2 = (BubbleTextVew) findViewById(R.id.description_2);
        description_3 = (BubbleTextVew) findViewById(R.id.description_3);
        description_4 = (BubbleTextVew) findViewById(R.id.description_4);

        description_1.setText(Html.fromHtml("<b>@01Comandos</b><br>¡Hola! Soy @01Comandos, Diseñador de interfaces humanas y Experiencia de Usuario con más de 3 años de experiencia. <font color='#29ABE2'>#MejorUX #FrontEnd</font>"));
        description_2.setText(Html.fromHtml("<b>@soyLuishp</b><br>Android Developer desde el principio de los tiempos, además de desarrollador #BackEnd. Master Developer en <font color='#29ABE2'>#BitDayCity #LoveCleanCode</font>"));
        description_3.setText(Html.fromHtml("<b>Jose Torrens</b><br>Especializado en <font color='#29ABE2'>#BackEnd #FrontEnd</font> con más de 4 años de experiencia en desarrollo de proyectos en PHP, JS, JAVA, NodeJs."));
        description_4.setText(Html.fromHtml("<b>Alejandro Venegas</b><br><font color='#29ABE2'>#Colaborador #FotografoCumanes</font>"));

        BitmapDrawable drawable = (BitmapDrawable) fondo.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap blurred = blurRenderScript(bitmap, 5);
        fondo.setImageBitmap(blurred);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        title = (TextView) toolbar.findViewById(R.id.title);

        title.setText(getString(R.string.desarrolladores));
        title.setVisibility(View.VISIBLE);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Desarrolladores");

        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        findViewById(R.id.l_d_1).setOnClickListener(this);
        findViewById(R.id.l_d_2).setOnClickListener(this);
        findViewById(R.id.l_d_3).setOnClickListener(this);

        findViewById(R.id.t_d_1).setOnClickListener(this);
        findViewById(R.id.t_d_2).setOnClickListener(this);
        findViewById(R.id.t_d_4).setOnClickListener(this);

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
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0){
            super.onBackPressed();
        }else{
            getFragmentManager().popBackStack();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
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


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.t_d_1:
            case R.id.t_d_2:
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + v.getContentDescription().toString())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/#!/" + v.getContentDescription().toString())));
                }
                break;

            case R.id.l_d_1:
            case R.id.l_d_2:
            case R.id.l_d_3:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(""+v.getContentDescription().toString())));
                break;
        }
    }
}
