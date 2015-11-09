package com.cumana.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cumana.bitmap.converteBitmap;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.struct.persons;
import com.cumana.struct.places;
import com.github.library.bubbleview.BubbleImageView;
import com.github.library.bubbleview.BubbleTextVew;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Javier on 23-10-2015.
 */
public class adapter_persons extends RecyclerView.Adapter<adapter_persons.AnimeViewHolder> {
    private List<persons> items;

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public BubbleTextVew description;

        public AnimeViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            description = (BubbleTextVew) v.findViewById(R.id.description);
        }
    }

    public adapter_persons(List<persons> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getPosition();
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if((i%2)==0) {
            Log.w("direction","izquierda");
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bubble_left, viewGroup, false);
            return new AnimeViewHolder(v);
        }else{
            Log.w("direction","derecha");
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bubble_right, viewGroup, false);
            return new AnimeViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder viewHolder, int i) {
        viewHolder.image.setImageBitmap(new converteBitmap().StringToBitMap(items.get(i).getPhoto()));
        viewHolder.description.setText(Html.fromHtml("<b>"+items.get(i).getName()+"</b><br>"+items.get(i).getResumen()+"<br><font color='#29ABE2'>#"+items.get(i).getProfession()+"</font>"));
    }
}