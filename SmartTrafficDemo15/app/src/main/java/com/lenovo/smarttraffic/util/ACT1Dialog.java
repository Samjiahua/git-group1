package com.lenovo.smarttraffic.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.lenovo.smarttraffic.R;

public class ACT1Dialog {

    public static Dialog dialog;

    public static void showDialog(Context context){
        dialog = new Dialog(context, R.style.custom_dialog2);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.act1_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void disDialog(){
        dialog.dismiss();
    }
}
