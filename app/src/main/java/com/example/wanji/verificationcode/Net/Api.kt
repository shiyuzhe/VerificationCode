package com.example.wanji.verificationcode.Net

import android.util.Log
import com.example.wanji.verificationcode.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *   by  :   syz
 *   Time: 2018/11/7 13:55
 *   Description:
 */
object Api {
    fun retrofit(): ApiStores {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor)
        }
        builder.addInterceptor(addHeaderInterceptor())
        builder.networkInterceptors()
        val okHttpClient = builder.build()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://wanjisoft.com:7450/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        //ApiStores::class.java取得对象的 Java 类
        return retrofit.create(ApiStores::class.java)
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url().toString()
            Log.e("okHttp", "url:${url.replace("%22","")}")
            val requestBuilder = originalRequest.newBuilder()
                    // Provide your custom header here
                    .header("Content-Type", "text/html;charset=UTF-8")
                    .header("Content-Disposition", "form-data; name=\\\"\"+app+\"\\\"")
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()

            chain.proceed(request)
        }
    }

}