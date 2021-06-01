package com.study.network

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection

internal class PhotoRequestDispatcher : Dispatcher() {

  override fun dispatch(request: RecordedRequest): MockResponse {
    return when  {
      (request.path?.startsWith("/v2/list") == true) ->
        MockResponse()
          .setResponseCode(HttpURLConnection.HTTP_OK)
          .setBody(getJson("response/photo_response.json"))
      else -> throw IllegalArgumentException("Unknown Request Path ${request.path}")
    }
  }
}
