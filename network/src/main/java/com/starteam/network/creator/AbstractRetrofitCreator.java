package com.starteam.network.creator;

import com.ea.net.interceptor.DownloadProgressInterceptor;
import com.ea.net.interceptor.UpLoadProgressInterceptor;
import com.ea.net.response.GsonConverterFixStringFactory;
import com.ea.net.response.IResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>Created by gizthon on 2018/3/21. email:2013mzhou@gmail.com</p>
 * <p>
 * des: T 表示默认的ApiInterface
 */
public abstract class AbstractRetrofitCreator<T> {

    private HashMap<String, T> cacheHttpMap = new HashMap<>();
    private HashMap<String, T> cacheHttpsMap = new HashMap<>();

    private AbstractRetrofitBuilder abstractRetrofitBuilder;
    private AbstractRetrofitBuilder defaultBuilder = new AbstractRetrofitBuilderImpl();


    public HashMap<String, T> getCacheHttpMap() {
        return cacheHttpMap;
    }

    public HashMap<String, T> getCacheHttpsMap() {
        return cacheHttpsMap;
    }

    /**
     * 请求相关Service
     *
     * @return
     */

    public T getHttpApiService() {
        return getApiService(cacheHttpsMap, null, false, null, null);
    }

    public T getHttpApiService(Class<T> clazz) {
        return getApiService(cacheHttpMap, clazz, false, null, null);
    }

    public T getHttpsApiService() {
        return getApiService(cacheHttpsMap, null, true, null, null);
    }

    public T getHttpsApiService(Class<T> clazz) {
        return getApiService(cacheHttpsMap, clazz, true, null, null);
    }

    /**
     * 下载相关Service
     *
     * @param clazz
     * @param downloadProgressListener
     * @return
     */

    public T getHttpsApiService(Class<T> clazz, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        return getApiService(cacheHttpsMap, clazz, true, null, downloadProgressListener);
    }

    public T getHttpsApiService(DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        return getApiService(cacheHttpsMap, null, true, null, downloadProgressListener);
    }


    public T getHttpApiService(Class<T> clazz, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        return getApiService(cacheHttpsMap, clazz, false, null, downloadProgressListener);
    }

    public T getHttpApiService(DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        return getApiService(cacheHttpsMap, null, false, null, downloadProgressListener);
    }


    /**
     * 上传相关Service
     *
     * @param uploadUploadListener
     * @return
     */
    public T getHttpsApiService(UpLoadProgressInterceptor.UploadListener uploadUploadListener) {
        return getApiService(cacheHttpsMap, null, true, uploadUploadListener, null);
    }

    public T getHttpsApiService(Class<T> clazz, UpLoadProgressInterceptor.UploadListener uploadUploadListener) {
        return getApiService(cacheHttpsMap, clazz, true, uploadUploadListener, null);
    }

    public T getHttpApiService(Class<T> clazz, UpLoadProgressInterceptor.UploadListener uploadUploadListener) {
        return getApiService(cacheHttpsMap, clazz, false, uploadUploadListener, null);
    }

    public T getHttpApiService(UpLoadProgressInterceptor.UploadListener uploadUploadListener) {
        return getApiService(cacheHttpsMap, null, false, uploadUploadListener, null);
    }


    public T createDefaultService(Class<T> serviceClass, String baseUrl, Class<? extends IResponse> responseClass, UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        OkHttpClient.Builder httpClient = getDefaultOkHttp(uploadUploadListener, downloadProgressListener);
        return createService(httpClient, serviceClass, baseUrl, responseClass, uploadUploadListener, downloadProgressListener);
    }


    public T getApiService(Map<String, T> cacheMap, Class<T> clazz, boolean isHttps, UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        T result = null;

        if (clazz == null) {
            clazz = getApiInterface();
        }

        if (clazz == null) {
            throw new NullPointerException("必须指定ApiInterface的类");
        }

        if (isCacheApiService() && cacheMap.containsKey(clazz.getName())) {
            result = cacheMap.get(clazz.getName());
        }
        if (result == null) {
            result = createDefaultService(clazz, getBaseUrl(isHttps), getResponseCls(), uploadUploadListener, downloadProgressListener);

            if (isCacheApiService()) {
                if (uploadUploadListener != null || downloadProgressListener != null) {
                    //不缓存带进度条的
                } else {
                    cacheMap.put(clazz.getName(), result);
                }
            } else {
                setCacheApiService(false);
            }
        }
        return result;
    }

    public <S> S createService(OkHttpClient.Builder httpClient, Class<S> serviceClass, String baseUrl, Class<? extends IResponse> responseClass, UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFixStringFactory.create(buildGson(), responseClass))
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }


    public Gson buildGson() {
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


    public OkHttpClient.Builder getDefaultOkHttp(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener) {
        OkHttpClient.Builder httpClient;
        if (abstractRetrofitBuilder != null) {
            httpClient = abstractRetrofitBuilder.getDefaultOkHttp(uploadUploadListener, downloadProgressListener);
            setCache(httpClient);
            setHttps(httpClient);
            setInterceptor(uploadUploadListener, downloadProgressListener, httpClient);
            abstractRetrofitBuilder = null;
        } else {

            httpClient = new OkHttpClient.Builder();
            //缓存设置
            setCache(httpClient);

            //设置https
            setHttps(httpClient);

            //设置超时
            setTimeout(uploadUploadListener, downloadProgressListener, httpClient);

            setInterceptor(uploadUploadListener, downloadProgressListener, httpClient);
        }

        return httpClient;
    }

    @Deprecated
    public void setInterceptor(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient) {
        //设置拦截器
        setLogger(httpClient);

        //上传下载监听
        setTransferListener(uploadUploadListener, downloadProgressListener, httpClient);
    }
    @Deprecated
    public void setTransferListener(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient) {
        defaultBuilder.setTransferListener(uploadUploadListener, downloadProgressListener, httpClient);
    }
    @Deprecated
    public void setLogger(OkHttpClient.Builder httpClient) {
        defaultBuilder.setLogger(httpClient);
    }

    @Deprecated
    public void setCache(OkHttpClient.Builder httpClient) {
        defaultBuilder.setCache(httpClient);
    }
    @Deprecated
    public void setHttps(OkHttpClient.Builder httpClient) {
        defaultBuilder.setHttps(httpClient);
    }
    @Deprecated
    public void setTimeout(UpLoadProgressInterceptor.UploadListener uploadUploadListener, DownloadProgressInterceptor.DownloadProgressListener downloadProgressListener, OkHttpClient.Builder httpClient) {
        if(abstractRetrofitBuilder != null){
            abstractRetrofitBuilder.setTimeout(uploadUploadListener, downloadProgressListener, httpClient);
            return;
        }

        defaultBuilder.setTimeout(uploadUploadListener, downloadProgressListener, httpClient);
    }


    public abstract String getBaseUrl(boolean isHttps);

    public abstract Class<? extends IResponse> getResponseCls();

    public abstract Class<T> getApiInterface();

    public boolean isCacheApiService() {

        if(abstractRetrofitBuilder != null){
          return   abstractRetrofitBuilder.isCacheApiService();
        }

        return defaultBuilder.isCacheApiService();
    }

    @Deprecated
    public void setCacheApiService(boolean cacheApiService) {
          defaultBuilder.setCacheApiService(cacheApiService);
    }

    /**
     * 构造新的builder，主要用来修改okhttpclient
     *
     * @param builder
     * @return
     */
    public AbstractRetrofitCreator<T> builder(AbstractRetrofitBuilder builder) {
        this.abstractRetrofitBuilder = builder;
        return this;
    }

}
