package com.starteam.network.transformer;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;

/**
 * Created by bailong.Lai on 2019-08-27.
 */
public class JSONTransform<T> implements FlowableTransformer<JSONObject, T> {
    private Class<T> tClass;

    public JSONTransform(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public Publisher<T> apply(Flowable<JSONObject> upstream) {


        return upstream.map(new Function<JSONObject, T>() {
            @Override
            public T apply(JSONObject jsonObject) throws Exception {

                T t = null;
                try {
                    if (!TextUtils.isEmpty(jsonObject.toString())) {
                        Gson gson = new Gson();
                        t = gson.fromJson(jsonObject.toString(), tClass);
                    }
                } catch (JsonSyntaxException var4) {
                    var4.printStackTrace();
                }

                if (t != null) {
                    return t;
                } else {
                    return tClass.newInstance();
                }
            }
        });
    }
}