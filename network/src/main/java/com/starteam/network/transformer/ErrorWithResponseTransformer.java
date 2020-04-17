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
public class ErrorWithResponseTransformer<R> implements FlowableTransformer<IResponse<R>, IResponse<R>>, IResponseHandler {

    public boolean responseSuccess(@NonNull IResponse response) {
        return response.isSuccess();
    }

    @Override
    public Publisher<IResponse<R>> apply(@NonNull Flowable<IResponse<R>> upstream) {
        return upstream.map(new Function<IResponse<R>, IResponse<R>>() {
            @Override
            public IResponse<R> apply(@NonNull IResponse<R> tResponse) throws Exception {
                if (tResponse == null) {
                    throw new EmptyException("tResponse", 0);
                }
                if (!responseSuccess(tResponse)) {
                    throw new ServerException(tResponse);
                } else {

                    return tResponse;
                }
            }
        }).onErrorResumeNext(new HttpResponseFunc<IResponse<R>>());
    }
}
