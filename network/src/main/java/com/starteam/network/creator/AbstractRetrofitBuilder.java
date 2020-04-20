package com.starteam.network.creator;



import com.starteam.network.interceptor.DownloadProgressInterceptor;
import com.starteam.network.interceptor.UpLoadProgressInterceptor;

import okhttp3.OkHttpClient;


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
