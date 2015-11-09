package com.cumana.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cumana.cumana500.R;
import com.cumana.fonts.TextView;
import com.cumana.sqlite.SQLiteHelper;
import com.cumana.tables.ciudad;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Javier on 23-10-2015.
 */
public class fragment_history extends Fragment implements  ViewPagerEx.OnPageChangeListener{

    SQLiteHelper sqlite;
    List<ciudad> queryCiudad = new ArrayList<>();
    TextView historia;

    private SliderLayout mDemoSlider;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_cumana, container, false);

        historia = (TextView) view.findViewById(R.id.historia);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);

        setHasOptionsMenu(true);
        sqlite = SQLiteHelper.getHelper(getActivity());

        queryCiudad = sqlite.getCiudad();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.mipmap.bg_developers);
        file_maps.put("Big Bang Theory",R.mipmap.bg_menu);
        file_maps.put("House of Cards", R.mipmap.intro_1);
        file_maps.put("Game of Thrones", R.mipmap.intro_2);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.addOnPageChangeListener(this);
        historia.setText(Html.fromHtml(queryCiudad.get(0).getHistory()));

        return view;
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
