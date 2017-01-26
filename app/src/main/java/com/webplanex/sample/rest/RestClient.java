package com.webplanex.sample.rest;

import com.webplanex.sample.utils.APIConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static WebService getMyWebservice() {

        OkHttpClient.Builder okClient = new OkHttpClient.Builder();
        okClient.connectTimeout(60000, TimeUnit.MILLISECONDS);
        okClient.writeTimeout(60000, TimeUnit.MILLISECONDS);
        okClient.readTimeout(60000, TimeUnit.MILLISECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okClient.interceptors().add(interceptor);

        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Response response = chain.proceed(chain.request());
                response.newBuilder()
                        .header("Cache-Control", "only-if-cached")
                        .build();
                return response;
            }
        });

        Retrofit client = new Retrofit.Builder()
                .baseUrl(APIConstant.BASE_URL)
                .client(okClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return client.create(WebService.class);
    }
}
