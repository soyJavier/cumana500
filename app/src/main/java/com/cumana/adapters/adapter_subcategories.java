package com.cumana.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.cumana.cumana500.R;
import com.cumana.fonts.CheckBox;
import com.cumana.fonts.TextView;
import com.cumana.struct.subcategories;
import java.util.ArrayList;

/**
 * Created by Javier on 15-11-2015.
 */
public class adapter_subcategories extends BaseAdapter {

    Activity activity;
    ArrayList<subcategories> cat;

    public adapter_subcategories(Activity activity, ArrayList<subcategories> cat) {
        this.activity = activity;
        this.cat = cat;
    }

    @Override
    public int getCount() {
        return cat.size();
    }

    @Override
    public Object getItem(int position) {
        return cat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cat.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.subcategories, null);
        }

        subcategories items = this.cat.get(position);

        TextView name = (TextView) vi.findViewById(R.id.options);
        CheckBox check = (CheckBox) vi.findViewById(R.id.check);

        name.setText(items.getCategories());
        check.setChecked(items.getCheck());

        return vi;
    }
}

