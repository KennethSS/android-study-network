package com.study.network.retrofit

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.study.network.retrofit.interceptor.NetworkState
import com.study.network.retrofit.interceptor.application.CacheInterceptor
import com.study.network.retrofit.interceptor.application.ForceCacheInterceptor
import com.study.network.retrofit.interceptor.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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
    private var isDebug: Boolean = true
    private var networkState: NetworkState? = null

    private const val CONNECTION_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L

    inline fun <reified T> provideService(url: String): T =
        getRetrofit(buildOkHttpInterceptor(), url).create(T::class.java)

    fun init(isDebug: Boolean = false,
             networkState: NetworkState? = null) {
        this.isDebug = isDebug
        this.networkState = networkState
    }

    fun buildOkHttpInterceptor(): OkHttpClient = run {
        OkHttpClient.Builder().run {
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            if (isDebug) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }

            networkState?.let { networkState ->
                addInterceptor(ForceCacheInterceptor(networkState))
                addNetworkInterceptor(NetworkConnectionInterceptor(networkState))
            }

            build()
        }
    }


    fun getRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .baseUrl(baseUrl)
            .build()
    }


    private fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    private fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}