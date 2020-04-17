package com.starteam.network.transformer;


import com.starteam.network.response.IResponse;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * Created by winterhuang on 2017/7/24.
 */
@Deprecated
public class DefaultTransformer<T> implements FlowableTransformer<IResponse<T>, T> {

    @Override
    public Publisher<T> apply(@NonNull Flowable<IResponse<T>> upstream) {
        return upstream
                .compose(new SchedulerTransformer<IResponse<T>>())
                .compose(new ErrorTransformer<T>());
    }
}
