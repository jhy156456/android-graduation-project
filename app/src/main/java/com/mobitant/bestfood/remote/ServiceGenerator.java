package com.mobitant.bestfood.remote;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobitant.bestfood.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * 서버와 통신하기 위한 리트로핏을 사용하기 위한 클래스
 */
public class ServiceGenerator {
    //로그인
    public static RemoteService getRetrofit(){

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(RemoteService.BASE_URL)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RemoteService.class);

    }
    public static RemoteService getRetrofit(String email, String password) {

        String credentials = email + ":" + password;
        String basic = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.NO_WRAP);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("Authorization", basic)
                    .method(original.method(),original.body());
            return  chain.proceed(builder.build());

        });
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(RemoteService.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RemoteService.class);
    }
    public static RemoteService getRetrofit(String token) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("x-access-token", token)
                    .method(original.method(),original.body());
            return  chain.proceed(builder.build());

        });

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(RemoteService.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RemoteService.class);
    }

        //로그인끝
    /**
     * 원격 호출을 정의한 인터페이스 메소드를 호출할 수 있는 서비스를 생성
     * @param serviceClass 원격 호출 메소드를 정의한 인터페이스
     * @return 인터페이스 구현체
     */
    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RemoteService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        return retrofit.create(serviceClass);
    }
}
