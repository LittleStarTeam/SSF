package com.starteam.network.response;

import android.net.ParseException;
import android.util.MalformedJsonException;

import com.google.gson.JsonSyntaxException;
import com.starteam.network.exception.EmptyException;
import com.starteam.network.exception.ServerException;
import com.starteam.network.utils.LogUtils;
import com.starteam.network.utils.ToastUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.annotations.NonNull;
import io.reactivex.subscribers.DefaultSubscriber;
import retrofit2.HttpException;

/**
 * Created by winterhuang on 2017/7/24.
 */

public class EADefaultSubscriber<T> extends DefaultSubscriber<T> implements IResponseSubscribe{
    protected static final int ERROR_THROWABLE_NULL = -10000;//Throwable 为空
    protected static final int ERROR_IRESPONSE_NULL = -10001;//Observer<IResponse>  为空
    protected static final int ERROR_PARSE = -10002;//数据解析失败
    protected static final int ERROR_CONNECT = -10003;//连接失败
    protected static final int ERROR_NETWORK = -10004;//网络错误
    protected static final int ERROR_UNKOWN = -10005;//未知错误

    @Override
    public void onNext(@NonNull T value) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (null == e) {
            onError(ERROR_THROWABLE_NULL, "");
            return;
        }
        LogUtils.e("" + e);

        if (e instanceof ServerException) {//服务器状态码 与response 对应的isSuccess 不匹配，一般都是业务错误
            ServerException error = (ServerException) e;
            IResponse response = error.getResponse();
            LogUtils.e(error.getMessage());
            this.onError(response.getCode(), response.getMessage());
            return;
        }

        if (e instanceof EmptyException) {
            EmptyException error = (EmptyException) e;
            LogUtils.e(error.getMessage());
            this.onError(ERROR_IRESPONSE_NULL, error.getMessage());
            return;
        }

        if (!(e instanceof HttpException) && !(e instanceof SocketTimeoutException)) {
            if (!(e instanceof MalformedJsonException) && !(e instanceof JSONException) && !(e instanceof ParseException) && !(e instanceof JsonSyntaxException) && !(e instanceof UnsupportedOperationException)) {
                if (e instanceof ConnectException) {
                    LogUtils.e("连接失败" + e.getMessage());
                    this.onError(ERROR_CONNECT, e.getMessage());
                } else {
                    LogUtils.e("未知错误" + e.getMessage());
                    this.onError(ERROR_UNKOWN, e.getMessage());

                    if (e.toString().startsWith("junit.framework")){//单元测试异常
                        throw new RuntimeException(e);
                    }
                }
            } else {
                LogUtils.e("解析错误:" + e.getMessage());
                this.onError(ERROR_PARSE, e.getMessage());
            }
        } else {
            LogUtils.e("网络错误:" + e.getMessage());
            this.onError(ERROR_NETWORK, e.getMessage());
        }

    }

    @Override
    public void onComplete() {

    }

    /**
     * 异常错误需要配置不同的提示文案或者页面错误标识
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public void onError(int code, String message) {
        switch (code) {
            case ERROR_THROWABLE_NULL:////Throwable 为空
                break;

            case ERROR_IRESPONSE_NULL://Observer<IResponse>  为空
                ToastUtils.show("服务器繁忙,请稍后重试!");
                break;

            case ERROR_PARSE://数据解析失败
                ToastUtils.show("数据解析失败");
                break;

            case ERROR_CONNECT://连接失败
                ToastUtils.show("额,网络好像出错了！请检查网络。");
                break;

            case ERROR_NETWORK://网络错误
                ToastUtils.show("额,网络好像出错了！请检查网络。");
                break;

            case ERROR_UNKOWN:
                break;

        }
    }

    public void onResult(int state, String msg){

    }

    @Override
    public void setResult(int state, String msg) {
        onResult(state, msg);
    }
}
