package com.starteam.network.creator;

import com.ea.net.interceptor.DownloadProgressInterceptor;
import com.ea.net.interceptor.UpLoadProgressInterceptor;

import okhttp3.OkHttpClient;

/**
 * <p>Created by gizthon on 2018/7/9. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public interface AbstractRetrofitBuilder {

    OkHttpClient.Builder getDefaultOkHttp(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener);

    //缓存设置
    void setCache(OkHttpClient.Builder httpClient);

    //设置https
    void setHttps(OkHttpClient.Builder httpClient);

    //设置超时
    void setTimeout(UpLoadProgressInterceptor.UploadListener uploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient);

    void setInterceptor(UpLoadProgressInterceptor.UploadListener uploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient);

    void setTransferListener(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient);

    void setLogger(OkHttpClient.Builder httpClient);


    boolean isCacheApiService();

    void setCacheApiService(boolean cacheApiService);

}
