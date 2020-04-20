package com.starteam.network.transformer;


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
 * 成功判断处理 默认 Transformer ，主要处理是否符合业务逻辑成功
 */

public class SuccessDefaultTransformer<T> implements FlowableTransformer<IResponse<T>, T> {

    private IResponseHandler responseHandler;

    public SuccessDefaultTransformer() {
    }

    public SuccessDefaultTransformer(IResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public Publisher<T> apply(@NonNull Flowable<IResponse<T>> upstream) {
        return upstream
                .compose(new SchedulerTransformer<IResponse<T>>())
                .compose(new ErrorTransformer<T>());
    }

    private class ErrorTransformer<R> implements FlowableTransformer<IResponse<R>, R> {

        private boolean responseSuccess(@NonNull IResponse response) {
            if (null == responseHandler) {
                return response.isSuccess();
            }
            return response.isSuccess() || responseHandler.responseSuccess(response);
        }


        @Override
        public Publisher<R> apply(@NonNull Flowable<IResponse<R>> upstream) {
            return upstream.map(new Function<IResponse<R>, R>() {
                @Override
                public R apply(@NonNull IResponse<R> tResponse) throws Exception {
                    if (tResponse == null) {
                        throw new EmptyException("tResponse", 0);
                    }

                    if (!responseSuccess(tResponse)) {
                        throw new ServerException(tResponse);
                    }

                    R r = tResponse.getContent();

                    if (r == null) {
                        throw new ServerException(tResponse);
                    } else {
                        //#fix 处理泛型为org.json.JSONObject返回数据为空的时候，返回正确的值，防止崩溃
                        // 建议使用 com.alibaba.fastjson.JSONObject 作为泛型接收泛型结果
                        if ((r.getClass().getName().equals("org.json.JSONObject$1")
                                && r.toString().equals("null"))) {
                            return (R) new JSONObject();
                        }
                        return r;
                    }

                }
            }).onErrorResumeNext(new HttpResponseFunc<R>());
        }

    }

}

