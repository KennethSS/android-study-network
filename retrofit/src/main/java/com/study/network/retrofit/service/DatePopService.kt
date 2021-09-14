package com.study.network.retrofit.service

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DatePopService {
	
	@Multipart
	@POST("v1/zone/regist/")
	suspend fun registerBeacon(
		@Part file: List<MultipartBody.Part>,
	): Any
}