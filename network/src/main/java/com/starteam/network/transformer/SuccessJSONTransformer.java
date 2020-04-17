package com.starteam.network.transformer;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.starteam.network.exception.EmptyException;
import com.starteam.network.exception.ServerException;
import com.starteam.network.response.HttpResponseFunc;
import com.starteam.network.response.IResponse;
import com.starteam.network.response.IResponseHandler;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by winterhuang on 2017/7/24.
 * <p>
 * <p>
 * 成功判断处理 默认 Transformer ，主要处理是否符合业务逻辑成功
 * 并且将入参是json转换成对应的实体类
 */

public class SuccessJSONTransformer<T> implements FlowableTransformer<IResponse<JSONObject>, T> {

    private IResponseHandler responseHandler;
    private Class<T> tClass;

    public SuccessJSONTransformer(Class<T> tClass) {
        this.tClass = tClass;
    }

    public SuccessJSONTransformer(Class<T> tClass,IResponseHandler responseHandler) {
        this.tClass = tClass;
        this.responseHandler = responseHandler;
    }

    @Override
    public Publisher<T> apply(@NonNull Flowable<IResponse<JSONObject>> upstream) {
        return upstream
                .compose(new SchedulerTransformer<IResponse<JSONObject>>())
                .compose(new ErrorTransformer<T>(tClass));
    }

    private class ErrorTransformer<R> implements FlowableTransformer<IResponse<JSONObject>, R> {

        private Class<R> tClass;

        public ErrorTransformer(Class<R> tClass) {
            this.tClass = tClass;
        }

        private boolean responseSuccess(@NonNull IResponse response) {
            if (null == responseHandler) {
                return response.isSuccess();
            }
            return response.isSuccess() || responseHandler.responseSuccess(response);
        }


        @Override
        public Publisher<R> apply(@NonNull Flowable<IResponse<JSONObject>> upstream) {
            return upstream.map(new Function<IResponse<JSONObject>, R>() {
                @Override
                public R apply(@NonNull IResponse<JSONObject> tResponse) throws Exception {
                    if (tResponse == null) {
                        throw new EmptyException("tResponse", 0);
                    }

                    if (!responseSuccess(tResponse)) {
                        throw new ServerException(tResponse);
                    }

                    JSONObject jsonObject = tResponse.getContent();

                    if (jsonObject == null) {
                        throw new ServerException(tResponse);
                    } else {
                        //#fix 处理泛型为org.json.JSONObject返回数据为空的时候，返回正确的值，防止崩溃
                        // 建议使用 com.alibaba.fastjson.JSONObject 作为泛型接收泛型结果
                        if ((jsonObject.getClass().getName().equals("org.json.JSONObject$1")
                                && jsonObject.toString().equals("null"))) {
                            return tClass.newInstance();
                        }

                        R t = null;
                        try {
                            if (!TextUtils.isEmpty(jsonObject.toString())) {
                                //这里使用的是gson 解析
                                Gson gson = new Gson();
                                t = gson.fromJson(jsonObject.toString(), tClass);
                            }
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }

                        if (t != null) {
                            return t;
                        } else {
                            return tClass.newInstance();
                        }
                    }

                }
            }).onErrorResumeNext(new HttpResponseFunc<R>());
        }

    }

}

