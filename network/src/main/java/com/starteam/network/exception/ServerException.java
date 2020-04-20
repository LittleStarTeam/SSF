package com.starteam.network.exception;

import com.starteam.network.response.IResponse;


public class ServerException extends RuntimeException {
    private IResponse iResponse;

    public ServerException(IResponse iResponse) {
        this.iResponse = iResponse;
    }

    public IResponse getResponse() {
        return iResponse;
    }

    public void  setResponse(IResponse iResponse) {
        this.iResponse = iResponse;
    }
}
