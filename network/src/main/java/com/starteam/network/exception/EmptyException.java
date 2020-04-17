package com.starteam.network.exception;

/**
 * <p>Created by gizthon on 2017/7/21. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class EmptyException extends RuntimeException {
    public int code;
    public String message;

    public EmptyException(String message, int code) {
        super(message);
        this.code = code;
    }

    public EmptyException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public EmptyException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
}