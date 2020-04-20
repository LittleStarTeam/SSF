package com.starteam.network.transformer;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class SchedulerTransformer<T> implements FlowableTransformer<T, T> {
    private Scheduler scheduler;

    public SchedulerTransformer() {
    }

    public SchedulerTransformer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Publisher<T> apply(@NonNull Flowable<T> upstream) {
        if (scheduler == null) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return upstream
                    .subscribeOn(scheduler)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }
}
