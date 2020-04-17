package com.starteam.network.transformer;


import com.starteam.network.exception.EmptyException;
import com.starteam.network.exception.ServerException;
import com.starteam.network.response.HttpResponseFunc;
import com.starteam.network.response.IResponse;
import com.starteam.network.response.IResponseHandler;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by winterhuang on 2017/7/24.
 */

public class SuccessResponseTransformer<T> implements FlowableTransformer<T, T> {

    private IResponseHandler responseHandler;

    public SuccessResponseTransformer() {
    }

    public SuccessResponseTransformer(IResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public Publisher<T> apply(@NonNull Flowable<T> upstream) {
        return upstream
                .compose(new SchedulerTransformer<T>())
                .compose(new ErrorTransformer<T>());
    }

    private class ErrorTransformer<R> implements FlowableTransformer<R, R> {

        private boolean responseSuccess(@NonNull R res) {
            if (res instanceof IResponse) {
                IResponse response = (IResponse) res;
                if (null == responseHandler) {
                    return response.isSuccess();
                }
                return response.isSuccess() || responseHandler.responseSuccess(response);
            }
            return false;

        }

        @Override
        public Publisher<R> apply(@NonNull Flowable<R> upstream) {
            return upstream.map(new Function<R, R>() {
                @Override
                public R apply(@NonNull R tResponse) throws Exception {
                    if (tResponse == null) {
                        throw new EmptyException("tResponse", 0);
                    }
                    if (!responseSuccess(tResponse)) {
                        throw new ServerException((IResponse) tResponse);
                    }

                    return tResponse;

                }
            }).onErrorResumeNext(new HttpResponseFunc<R>());
        }

    }

}

