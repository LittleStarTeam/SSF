package com.starteam.network.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <p>Created by gizthon on 2017/7/21. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class LogUtils {
    private static boolean DEBUG = true;
    public static final String TAG = "SDK";
    private static boolean isPrint = true;
    private static String className;
    private static String methodName;
    private static int lineNumber;
    private static SimpleDateFormat myLogSdf;
    private static SimpleDateFormat logfile;

    private LogUtils() {
    }



    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        if (TextUtils.isEmpty(className)){
            className = "";
        }else{
            if (className.length() > 5){
                className = className.substring(0, className.length() - 5);
            }
        };
        if (TextUtils.isEmpty(methodName)){
            methodName = "";
        }
        buffer.append("[at ");
        buffer.append(className);
        buffer.append(".");
        buffer.append(methodName);
        buffer.append("(");
        buffer.append(className);
        buffer.append(".java:");
        buffer.append(lineNumber);
        buffer.append(") ]\n");
        buffer.append(log);
        return buffer.toString();
    }



    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    /** @deprecated */
    @Deprecated
    private static void v(Object message) {
        if(isPrint) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.v(TAG, createLog(String.valueOf(message)));
        }

    }

    /** @deprecated */
    @Deprecated
    private static void d(Object message) {
        if(isPrint) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.d(TAG, createLog(String.valueOf(message)));
        }

    }

    public static void i(Object message) {
        if(isPrint) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.i(TAG, createLog(String.valueOf(message)));
        }

    }

    public static void w(Object message) {
        if(isPrint) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.w(TAG, createLog(String.valueOf(message)));
        }

    }

    public static void e(Object message) {
        if(isPrint) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.e(TAG, createLog(String.valueOf(message)));
        }

    }

    static {
        myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        logfile = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }
}
