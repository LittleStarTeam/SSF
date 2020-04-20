package com.starteam.network.transformer;

import android.app.Activity;


import androidx.fragment.app.Fragment;

import com.starteam.network.utils.LogUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;


public class LifecycleTransformer<T> implements FlowableTransformer<T, T> {
    private Object view;

    public LifecycleTransformer(Object object) {
        this.view = object;
    }

    public LifecycleTransformer() {
    }

    public Publisher<T> apply(@NonNull Flowable<T> upstream) {
        if (this.view instanceof LifecycleProvider) {

            if (this.view instanceof Activity){
                return upstream.compose(((LifecycleProvider)this.view).bindUntilEvent(ActivityEvent.DESTROY));
            }else if (this.view instanceof Fragment){
                return upstream.compose(((LifecycleProvider)this.view).bindUntilEvent(FragmentEvent.DESTROY_VIEW));
            }
            return upstream;

        } else {
            LogUtils.i("请注意 【" + this.view + "】 对象没有实现 LifecycleProvider  接口  ,容易造成Rxjava 的内存泄漏 ");
            return upstream;
        }
    }
}

