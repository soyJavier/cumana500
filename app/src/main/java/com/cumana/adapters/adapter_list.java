package com.cumana.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.struct.places;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Javier on 21-10-2015.
 */
public class adapter_list extends RecyclerView.Adapter<adapter_list.AnimeViewHolder> {

    private List<places> items;

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView subName;
        public TextView description;

        public AnimeViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            subName = (TextView) v.findViewById(R.id.subName);
            description = (TextView) v.findViewById(R.id.description);
        }
    }

    public adapter_list(List<places> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.places, viewGroup, false);
        return new AnimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder viewHolder, int i) {

        Picasso.with(items.get(i).getCtx()).load(items.get(i).getUrl()).into(viewHolder.image);

        viewHolder.name.setText(items.get(i).getName());

        if(items.get(i).getDescription().length()>0){

            if(items.get(i).getDescription().length()>=90){
                viewHolder.description.setText(items.get(i).getDescription().substring(0,90)+"...");
            }else{
                viewHolder.description.setText(items.get(i).getDescription());
            }
        }else{
            viewHolder.description.setVisibility(View.GONE);
        }

        if(items.get(i).getAutority()){

            if(items.get(i).getSubName() != null) {
                try {
                    JSONObject autority = new JSONObject(items.get(i).getSubName());

                    viewHolder.subName.setText(autority.getString("appointment") + " " + autority.getString("title") + " " + autority.getString("name") + " " + autority.getString("last"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            viewHolder.subName.setText(items.get(i).getSubName());
        }
    }
}
