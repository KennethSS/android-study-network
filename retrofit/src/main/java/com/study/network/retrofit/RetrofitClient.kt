package com.study.network.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Copyright 2020 Kenneth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/
object RetrofitClient {
    private const val CONNECTION_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L

    inline fun <reified T>provideService(isDebug: Boolean): T  {
        val retrofit = getRetrofit(
            buildOkHttpInterceptor(isDebug),
            ""
        )
        return retrofit.create(T::class.java)
    }

    fun buildOkHttpInterceptor(isDebug: Boolean): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

        if (isDebug) {
            httpClientBuilder
                .addNetworkInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
        }

        /*httpClientBuilder.addNetworkInterceptor { chain ->
            val request = chain.request()
            val builder: Request.Builder = request.newBuilder()
            chain.proceed(builder.build())
        }*/

        return httpClientBuilder.build()
    }

    fun getRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }
}