package com.edinburgh.ewireless.method.System;

import android.content.Context;
import android.widget.Toast;

public class ToastShow {
    private Context context;

    private Toast toast = null; //If the toast is excuting
    public ToastShow(Context context){
        this.context = context;
    }
    public void toastShow(String text){
        if (toast == null){
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        else {
            //Cancel the toast first to avoid the accumulation
            toast.cancel();
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
