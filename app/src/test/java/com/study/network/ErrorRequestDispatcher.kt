package com.study.network

import com.study.network.retrofit.exception.NetworkException
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

internal class ErrorRequestDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when  {
            (request.path?.startsWith("404") == true) -> throw NetworkException.NotFoundException("Not Found")
            else -> throw IllegalArgumentException("Unknown Request Path ${request.path}")
        }
    }
}