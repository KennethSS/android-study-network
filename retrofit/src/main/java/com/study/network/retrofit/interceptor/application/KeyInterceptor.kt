package com.study.network.retrofit.interceptor.application

import okhttp3.Interceptor
import okhttp3.Response

class KeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", "Some Key")
            .build()


        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}