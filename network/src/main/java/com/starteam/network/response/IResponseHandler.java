package com.starteam.network.response;

import io.reactivex.annotations.NonNull;

public interface IResponseHandler {

    boolean responseSuccess(@NonNull IResponse response);
}
