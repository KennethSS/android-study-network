package com.study.network.retrofit.interceptor.network

import okhttp3.Interceptor
import okhttp3.Response

class AddTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = ""

        val authenticationRequestBuilder = chain.request().newBuilder()


        if (token.isNotEmpty()) {
            authenticationRequestBuilder.addHeader(AUTHORIZATION, "$BEARER $token")
        }


        return chain.proceed(authenticationRequestBuilder.build())
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
        private const val JWT = "JWT"
    }
}