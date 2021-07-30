package com.study.network.service

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface BasicService {
  @Multipart
  @POST("feed/write/")
  suspend fun postFeed(
    @PartMap params: HashMap<String, RequestBody>): Any
}