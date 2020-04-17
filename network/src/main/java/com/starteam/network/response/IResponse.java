package com.starteam.network.response;

/**
 * <p>Created by gizthon on 2017/7/21. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public interface IResponse<T> {
    IResponse<T> parserObject2String(String jsonString);
    IResponse<T> parserObject2Integer(String jsonString);
    IResponse<T> parserObject2Boolean(String jsonString);
    IResponse<T> parserObject2Double(String jsonString);
    IResponse<T> parserObject2Long(String jsonString);

    IResponse<T> parserObject(String jsonString);

    boolean isSuccess();

    String getMessage();

    int getCode();

    T getContent();

}
