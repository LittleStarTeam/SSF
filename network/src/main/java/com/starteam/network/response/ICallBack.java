
package com.starteam.network.response;

public abstract class ICallBack<T> {
    public abstract void onSuccess(T entity);

    public void onFailed(int code , String message) {
    }
}
