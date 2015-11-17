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
import com.cumana.struct.services;
import com.github.library.bubbleview.BubbleTextVew;

import java.util.List;

/**
 * Created by Javier on 12-11-2015.
 */
public class adapter_services extends RecyclerView.Adapter<adapter_services.AnimeViewHolder> {
    private List<services> items;

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;

        public AnimeViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
        }
    }

    public adapter_services(List<services> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.services, viewGroup, false);
        return new AnimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder viewHolder, int i) {
        viewHolder.image.setImageResource(items.get(i).getIcon());
        viewHolder.name.setText(items.get(i).getName());
    }
}