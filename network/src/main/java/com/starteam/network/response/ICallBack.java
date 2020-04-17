
package com.starteam.network.response;

/**
 * <p>Created by gizthon on 2017/7/21. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public abstract class ICallBack<T> {
    public abstract void onSuccess(T entity);

    public void onFailed(int code , String message) {
    }
}
