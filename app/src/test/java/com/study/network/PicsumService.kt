package com.study.network

import com.study.network.response.PhotoItemResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumService {
    @GET("v2/list")
    fun fetchPhotoList(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20
    ): Single<List<PhotoItemResponse>>
}