package com.cumana.list;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Javier on 21-10-2015.
 */
public class details extends AppCompatActivity {

    JSONObject json;
    TextView details,title,address;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        details = (TextView) findViewById(R.id.description);
        title = (TextView) findViewById(R.id.title);
        address = (TextView) findViewById(R.id.address);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        try {
            json = new JSONObject(getIntent().getExtras().getString("details"));

            title.setText(json.getString("name"));
            details.setText(json.getString("description"));
            address.setText(json.getString("address"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

        }

        return super.onOptionsItemSelected(item);
    }
}
