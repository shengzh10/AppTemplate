package com.hitwh.apptemplate.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hitwh.apptemplate.common.util.Const;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author SQY
 * @since 2017/3/16.
 */

public class ApiRetrofit {

    /**
     * 创建一个用于retrofit的apiservice实例
     *
     * @param cls apiservice的class
     * @param <T> apiservice实例对象
     * @return
     */
    public static <T> T create(final Class<T> cls) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(Const.SERVER)//注意此处,设置服务器的地址
                .addConverterFactory(GsonConverterFactory.create(gson))//用于Json数据的转换,非必须
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//用于返回Rxjava调用,非必须
                .build();
        return retrofit.create(cls);
    }
}
