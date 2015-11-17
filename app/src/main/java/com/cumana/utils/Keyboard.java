package com.cumana.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Javier on 16-11-2015.
 */
public class Keyboard {

    Activity activity;

    public Keyboard(Activity activity){
        this.activity = activity;
    }


    public void hide() {
        InputMethodManager inputManager = (InputMethodManager) this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.activity.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
