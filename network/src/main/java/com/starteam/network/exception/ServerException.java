package com.starteam.network.exception;

import com.starteam.network.response.IResponse;

/**
 * <p>Created by gizthon on 2017/7/21. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
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
