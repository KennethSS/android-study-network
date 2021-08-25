package com.study.network.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface BasicService {
  @Multipart
  @POST("feed/write/")
  suspend fun postFeed(
    @Header("Authorization") header: String,
    @Part image: MultipartBody.Part,
    @PartMap params: HashMap<String, RequestBody>): Any
}