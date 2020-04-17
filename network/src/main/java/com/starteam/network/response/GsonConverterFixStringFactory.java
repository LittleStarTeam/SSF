package com.starteam.network.response;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * <p>Created by gizthon on 16/12/18. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */

public final class GsonConverterFixStringFactory extends Converter.Factory {
    private final Gson gson;
    private final Class<? extends IResponse> cls;
    private GsonConverterFixStringFactory(Gson gson, Class<? extends IResponse> cls) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        this.cls = cls;
    }

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFixStringFactory create() {
        return create(new Gson(),null);
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFixStringFactory create(Gson gson, Class<? extends IResponse> cls) {
        return new GsonConverterFixStringFactory(gson,cls);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {

        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));


        return new GsonResponseBodyConverter<>(gson, adapter, type,cls);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

    public  final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    public   final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final Type type;
        private final TypeAdapter<T> adapter;
        private Class<? extends IResponse> cls;

        GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type, Class<? extends IResponse> cls) {
            this.gson = gson;
            this.adapter = adapter;
            this.type = type;
            this.cls = cls;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {

            if (type == String.class) {
                try {
                    return (T) value.string();
                } finally {
                    value.close();
                }

            } else if (cls != null && type.toString().endsWith(cls.getCanonicalName() + "<java.lang.String>")
                    ) {

                try {
                    //返回这个对象,然后是string
                    IResponse response = cls.newInstance().parserObject2String(value.string());
                    return (T) response;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    value.close();
                }


            }else if (cls != null && type.toString().endsWith(cls.getCanonicalName() + "<java.lang.Boolean>")
                    ) {

                try {
                    //返回这个对象,然后是string
                    IResponse response = cls.newInstance().parserObject2Boolean(value.string());
                    return (T) response;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    value.close();
                }


            }else if (cls != null && type.toString().endsWith(cls.getCanonicalName() + "<java.lang.Integer>")
                    ) {

                try {
                    //返回这个对象,然后是string
                    IResponse response = cls.newInstance().parserObject2Integer(value.string());
                    return (T) response;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    value.close();
                }


            }else if (cls != null && type.toString().endsWith(cls.getCanonicalName() + "<java.lang.Long>")
                    ) {

                try {
                    //返回这个对象,然后是string
                    IResponse response = cls.newInstance().parserObject2Long(value.string());
                    return (T) response;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    value.close();
                }


            }else if (cls != null && type.toString().endsWith(cls.getCanonicalName() + "<java.lang.Double>")
                    ) {

                try {
                    //返回这个对象,然后是string
                    IResponse response = cls.newInstance().parserObject2Double(value.string());
                    return (T) response;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    value.close();
                }


            } else if (cls != null && (type.toString().endsWith(cls.getCanonicalName() + "<org.json.JSONObject>")
                    || type.toString().endsWith(cls.getCanonicalName() + "<org.json.JSONArray>"))
                    ) {
                //返回这个对象,然后是string

                try {
                    IResponse response = cls.newInstance().parserObject(value.string());
                    return (T) response;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    value.close();
                }


            } else {
                JsonReader jsonReader = gson.newJsonReader(value.charStream());
                try {
                    return adapter.read(jsonReader);
                } finally {
                    value.close();
                }
            }

            return null;
        }
    }
}