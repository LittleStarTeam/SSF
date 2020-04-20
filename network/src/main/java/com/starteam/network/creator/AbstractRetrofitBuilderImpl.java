package com.starteam.network.creator;



import com.starteam.network.https.BksInfo;
import com.starteam.network.https.HTTPSTrustManager;
import com.starteam.network.https.HttpsUtils;
import com.starteam.network.interceptor.DownloadProgressInterceptor;
import com.starteam.network.interceptor.HeaderInterceptor;
import com.starteam.network.interceptor.HttpLoggingInterceptor;
import com.starteam.network.interceptor.UpLoadProgressInterceptor;
import com.starteam.network.utils.AppInstanceUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


public class AbstractRetrofitBuilderImpl implements AbstractRetrofitBuilder {

    //需要缓存httpclient，调用的过程中是一次性的。默认为true,设置的时候调用前设置为false，调用完之后又会变成true
    protected boolean isCacheApiService = true;

    /**
     * default okhttp client impl
     */
    private static final int CACHE_SIZE = 1024 * 1024 * 50;//缓存大小

    public OkHttpClient.Builder getDefaultOkHttp(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //缓存设置
        setCache(httpClient);

        //设置https
        setHttps(httpClient);

        //设置超时
        setTimeout(uploadUploadListener, downloadProgressListener, httpClient);

        setInterceptor(uploadUploadListener, downloadProgressListener, httpClient);


        return httpClient;
    }


    @Override
    public void setInterceptor(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient) {
        //设置拦截器
        setLogger(httpClient);

        //上传下载监听
        setTransferListener(uploadUploadListener, downloadProgressListener, httpClient);
    }

    @Override
    public void setTransferListener(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient) {
        if (uploadUploadListener != null) {//上传拦截
            httpClient.addInterceptor(new UpLoadProgressInterceptor(uploadUploadListener));
        }
        if (downloadProgressListener != null) {//下载拦截
            httpClient.addInterceptor(new DownloadProgressInterceptor(downloadProgressListener));
        }
    }

    @Override
    public void setLogger(OkHttpClient.Builder httpClient) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);//日志拦截
    }


    @Override
    public boolean isCacheApiService() {
        return isCacheApiService;
    }

    @Override
    public void setCacheApiService(boolean cacheApiService) {
        this.isCacheApiService  = cacheApiService;
    }


    @Override
    public void setCache(OkHttpClient.Builder httpClient) {
        //缓存目录
        File cacheFile = new File(AppInstanceUtils.INSTANCE.getCacheDir(), "netcache");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        httpClient.cache(cache);
        httpClient.addInterceptor(new HeaderInterceptor());//头部拦截
    }

    @Override
    public void setHttps(OkHttpClient.Builder httpClient) {
        try {
            BksInfo bksInfo = new BksInfo(
                    AppInstanceUtils.INSTANCE.getAssets().open("starline2.bks"),
                    "123@eascs.com");
            //https
            httpClient.sslSocketFactory(HttpsUtils.getSslSocketFactory(
                    bksInfo.getInputStream(),
                    bksInfo.getBksPwd(),
                    bksInfo.getCrtInputStream()
            ), new HTTPSTrustManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTimeout(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient) {
        int CONNECT_TIME_OUT = 30;//连接超时，单位 秒
        int READ_TIME_OUT = 15;//读取超时，单位 秒
        int WRITE_TIME_OUT = 20;//写入超时，单位 秒
        if (uploadUploadListener != null || downloadProgressListener != null) {
            httpClient.connectTimeout(CONNECT_TIME_OUT + 60, TimeUnit.SECONDS);
            httpClient.readTimeout(READ_TIME_OUT + 6, TimeUnit.SECONDS);
            httpClient.writeTimeout(WRITE_TIME_OUT + 6, TimeUnit.SECONDS);
        } else {
            httpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
            httpClient.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
            httpClient.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
        }
    }
}
