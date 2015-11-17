package com.cumana.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.struct.places;
import com.cumana.utils.PlaceData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
    public void onBindViewHolder(final AnimeViewHolder viewHolder, int i) {

        final int p = i;


        viewHolder.image.setImageResource(R.mipmap.bg_load);

        Picasso.with(items.get(i).getCtx()).load(items.get(i).getUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                viewHolder.image.setImageBitmap(bitmap);
                new PlaceData().listPlaces.get(p).setBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.w("error", "error " + errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        viewHolder.name.setText(items.get(i).getName());

        if(items.get(i).getDescription().length()>0){

            String description = Html.fromHtml(items.get(i).getDescription()).toString();

            if(description.length()>=90){
                viewHolder.description.setText(description.substring(0, 90)+"...");
            }else{
                viewHolder.description.setText(description);
            }
        }else{
            viewHolder.description.setVisibility(View.GONE);
        }

        ((BitmapDrawable)viewHolder.image.getDrawable()).getBitmap();
        if(items.get(i).getAutority()){

            if(items.get(i).getSubName() != null) {
                viewHolder.subName.setText(items.get(i).getSubName());
            }
        }else{
            viewHolder.subName.setText(items.get(i).getSubName());
        }
    }
}
