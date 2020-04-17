package com.starteam.network.https;

import java.io.InputStream;

/**
 * Created by KevinHo on 2016/5/29 0029.
 */
public class BksInfo {
    InputStream bksInputStream;
    InputStream[] crtInputStream;
    String bksPwd;

    public BksInfo(InputStream inputStream, String bksPwd){
        this.bksInputStream = inputStream;
        this.bksPwd = bksPwd;
    }

    public BksInfo(InputStream bksInputStream, String bksPwd, InputStream... crtInputStream) {
        this.bksInputStream = bksInputStream;
        this.bksPwd = bksPwd;
        this.crtInputStream = crtInputStream;
    }

    public InputStream getInputStream() {
        return bksInputStream;
    }

    public String getBksPwd() {
        return bksPwd;
    }

    public InputStream[] getCrtInputStream() {
        return crtInputStream;
    }

    public void setCrtInputStream(InputStream... crtInputStream) {
        this.crtInputStream = crtInputStream;
    }
}
