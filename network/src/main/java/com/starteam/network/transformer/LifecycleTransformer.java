package com.starteam.network.transformer;

import com.starteam.network.utils.LogUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * Created by winterhuang on 2017/7/24.
 */

public class LifecycleTransformer<T> implements FlowableTransformer<T, T> {
    private Object view;

    public LifecycleTransformer(Object object) {
       this.view = object;
    }

    public LifecycleTransformer() {
    }

    @Override
    public Publisher<T> apply(@NonNull Flowable<T> upstream) {
        if (view instanceof LifecycleProvider) {
            return upstream.compose(((LifecycleProvider<?>)view).<T>bindToLifecycle());
        }
        LogUtils.i("请注意 【" + view + "】 对象没有实现 LifecycleProvider  接口  ,容易造成Rxjava 的内存泄漏 ");
        return upstream;
    }
}
