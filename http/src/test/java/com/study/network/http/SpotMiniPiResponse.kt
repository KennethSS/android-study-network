package com.study.network.http

data class SpotMiniPiResponse(
    var code: Int? = null,
    var data: List<Data>? = null,
    var message: String? = null
)