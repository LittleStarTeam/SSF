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
 * Created by winterhuang on 2017/9/29.
 */

@Deprecated
public class   ErrorTransformer<R> implements FlowableTransformer<IResponse<R>, R>, IResponseHandler {

    public boolean responseSuccess(@NonNull IResponse response) {
        return response.isSuccess();
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
                } else {
                    R r = tResponse.getContent();
                    if (r == null) {
//                        return (R) ((Class) ((ParameterizedType) tResponse.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
                        r = (R) new Object();
                    }
                    return r;
                }
            }
        }).onErrorResumeNext(new HttpResponseFunc<R>());
    }

}
