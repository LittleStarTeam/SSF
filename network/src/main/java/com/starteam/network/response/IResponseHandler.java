package com.starteam.network.response;

import io.reactivex.annotations.NonNull;

/**
 * Created by winterhuang on 2017/9/29.
 */

public interface IResponseHandler {

    boolean responseSuccess(@NonNull IResponse response);
}
