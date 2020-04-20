package com.starteam.network.response;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * ExceptionEngine为处理异常的驱动器
 */

public class HttpResponseFunc<T> implements Function<Throwable, Publisher<? extends T>> {
    @Override
    public Publisher<? extends T> apply(@NonNull Throwable throwable) throws Exception {
        return Flowable.error(throwable);
    }
}
