package com.study.network.retrofit.interceptor.application

import com.study.network.retrofit.exception.NetworkException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

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
class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)

        when(response.code) {
            in 200..299 -> {

            }
            400 -> throw NetworkException.BadRequestException("BadRequestException")
            401 -> throw NetworkException.UnauthorizedException("UnauthorizedException")
            403 -> throw NetworkException.ForbiddenException("ForbiddenException")
            404 -> throw NetworkException.NotFoundException("NotFoundException")
            else -> {

            }
        }

        return response
    }
}