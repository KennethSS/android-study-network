package com.study.network.service

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface BasicService {
	@Multipart
	@POST("feed/write/")
	suspend fun postFeed(
		@Header("Authorization") header: String,
		@Part image: MultipartBody.Part,
		@PartMap params: HashMap<String, RequestBody>
	): Any
	
	
	
	// Full Url 호출의 경우
	@GET
	suspend fun getNextUrlResponse(@Url url: String)
	
	// Url Path 에 파라미터를 넣을 경우
	@GET("api/comment/{id}")
	suspend fun getCommentListById(@Path("id") id: Int): Any
	
	// Patch 호출의 경우
	@PATCH("api/comment/{id}")
	suspend fun updateComment(@Path("id") id: Int, @Body json: JsonObject): Any
}