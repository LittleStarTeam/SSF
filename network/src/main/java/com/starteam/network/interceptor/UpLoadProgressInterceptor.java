package com.starteam.network.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 文件上传拦截器
 */
public class UpLoadProgressInterceptor implements Interceptor {
    private UpLoadProgressInterceptor.UploadListener progressUploadListener;

    public UpLoadProgressInterceptor(UpLoadProgressInterceptor.UploadListener progressUploadListener) {
        this.progressUploadListener = progressUploadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }

        Request progressRequest = originalRequest.newBuilder()
                .method(originalRequest.method(),
                        new UploadCountingRequestBody(originalRequest.body(), progressUploadListener))
                .build();

        return chain.proceed(progressRequest);
    }

    private class UploadCountingRequestBody extends RequestBody {

        protected RequestBody delegate;
        protected UploadListener uploadListener;

        protected CountingSink countingSink;


        public UploadCountingRequestBody(RequestBody delegate, UploadListener uploadListener ) {
            this.delegate = delegate;
            this.uploadListener = uploadListener;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            try {
                return delegate.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            BufferedSink bufferedSink;

            countingSink = new CountingSink(sink);
            bufferedSink = Okio.buffer(countingSink);

            delegate.writeTo(bufferedSink);

            bufferedSink.flush();
        }

        protected final class CountingSink extends ForwardingSink {

            private long bytesWritten = 0;
            private long contentLength = 0;

            public CountingSink(Sink delegate) {
                super(delegate);
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                bytesWritten += byteCount;
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                if (null != uploadListener){
                    uploadListener.onRequestProgress(  bytesWritten, contentLength, bytesWritten == contentLength);
                }
            }
        }

    }

    public interface UploadListener {

        void onRequestProgress(long bytesWritten, long contentLength, boolean isDone);
    }
}

