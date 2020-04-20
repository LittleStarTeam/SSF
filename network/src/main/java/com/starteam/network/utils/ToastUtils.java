package com.starteam.network.utils;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {
    private ToastUtils() {
    }

    public static void show(String message) {
        Application context = AppInstanceUtils.INSTANCE;
        if(null != context && !TextUtils.isEmpty(message)) {
            show(message, 17);
        }
    }

    public static void show(String message, int gravity) {
        Application context = AppInstanceUtils.INSTANCE;
        if(null != context && !TextUtils.isEmpty(message)) {
            Toast toast = Toast.makeText(context, message, 0);
            toast.setGravity(gravity, 0, 10);
            toast.show();
        }
    }
}