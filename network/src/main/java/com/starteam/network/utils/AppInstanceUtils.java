package com.starteam.network.utils;

import android.app.Application;

/**
 * <p>Created by gizthon on 2017/7/21. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class AppInstanceUtils {
    public static final Application INSTANCE;

    public AppInstanceUtils() {
    }

    static {
        Application app = null;

        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", new Class[0]).invoke((Object)null, new Object[0]);
            if(app == null) {
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
            }
        } catch (Exception var8) {
//            Log.e("", "Failed to get current application from AppGlobals." + var8.getMessage());

            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke((Object)null, new Object[0]);
            } catch (Exception var7) {
//                Log.e("", "Failed to get current application from ActivityThread." + var8.getMessage());
            }
        } finally {
            INSTANCE = app;
        }

    }
}
