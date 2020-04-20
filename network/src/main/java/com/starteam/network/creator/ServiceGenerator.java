package com.starteam.network.creator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.starteam.network.https.BksInfo;
import com.starteam.network.https.HTTPSTrustManager;
import com.starteam.network.https.HttpsUtils;
import com.starteam.network.interceptor.DownloadProgressInterceptor;
import com.starteam.network.interceptor.HeaderInterceptor;
import com.starteam.network.interceptor.HttpLoggingInterceptor;
import com.starteam.network.interceptor.UpLoadProgressInterceptor;
import com.starteam.network.response.GsonConverterFixStringFactory;
import com.starteam.network.response.IResponse;
import com.starteam.network.utils.AppInstanceUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    /**
     * 指定返回实体类，当responseClass != null 会回调 DefaultSubscribers.onError(int code, String message) 方法，否则反之
     *
     * @param serviceClass
     * @param baseUrl
     * @param responseClass
     * @param <S>
     * @return
     */

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Class<? extends IResponse> responseClass, UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        OkHttpClient.Builder httpClient = getDefaultOkHttp(uploadUploadListener, downloadProgressListener);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFixStringFactory.create(buildGson(), responseClass))
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }


    public static <S> S createService(OkHttpClient.Builder httpClient, Class<S> serviceClass, String baseUrl, Class<? extends IResponse> responseClass) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFixStringFactory.create(buildGson(), responseClass))
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private static Gson buildGson() {
        Gson gson = new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
//                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
//
//                .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
//                .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
//
//                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                .registerTypeAdapterFactory(new ListTypeAdapterFactory())
                .create();
        return gson;
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Class<? extends IResponse> responseClass, UpLoadProgressInterceptor.UploadListener uploadUploadListener) {
        return createService(serviceClass, baseUrl, responseClass, uploadUploadListener, null);
    }


    public static <S> S createService(Class<S> serviceClass, String baseUrl, Class<? extends IResponse> responseClass, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        return createService(serviceClass, baseUrl, responseClass, null, downloadProgressListener);
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Class<? extends IResponse> responseClass) {
        return createService(serviceClass, baseUrl, responseClass, null, null);
    }


    /**
     * default okhttp client impl
     */
    private static final int CACHE_SIZE = 1024 * 1024 * 50;//缓存大小
    private static final int CONNECT_TIME_OUT = 30;//连接超时，单位 秒
    private static final int READ_TIME_OUT = 15;//读取超时，单位 秒
    private static final int WRITE_TIME_OUT = 20;//写入超时，单位 秒

    public static OkHttpClient.Builder getDefaultOkHttp() {
        return getDefaultOkHttp(null, null);
    }

    public static OkHttpClient.Builder getDefaultOkHttp(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //缓存目录
        File cacheFile = new File(AppInstanceUtils.INSTANCE.getCacheDir(), "/NetCache");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        httpClient.cache(cache);

        //设置拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);//日志拦截
        if (uploadUploadListener != null) {//上传拦截
            httpClient.addInterceptor(new UpLoadProgressInterceptor(uploadUploadListener));
        }
        if (downloadProgressListener != null) {//下载拦截
            httpClient.addInterceptor(new DownloadProgressInterceptor(downloadProgressListener));
        }
        httpClient.addInterceptor(new HeaderInterceptor());//头部拦截


        //设置超时
        httpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        httpClient.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
        try {
            BksInfo bksInfo = new BksInfo(
                    AppInstanceUtils.INSTANCE.getAssets().open("starline2.bks"),
                    "123@eascs.com"
            );
            //https
            httpClient.sslSocketFactory(HttpsUtils.getSslSocketFactory(
                    bksInfo.getInputStream(),
                    bksInfo.getBksPwd(),
                    bksInfo.getCrtInputStream()
            ), new HTTPSTrustManager());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpClient;
    }


}
