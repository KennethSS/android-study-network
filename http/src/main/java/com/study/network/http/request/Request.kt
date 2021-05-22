package com.study.network.http.request

import com.study.network.http.ParameterType

data class Request(
    val method: String,
    val endPoint: String,
    val headers: HashMap<String, String> = hashMapOf(),
    val params: HashMap<String, Any> = hashMapOf(),
    val paramType: ParameterType = ParameterType.KEY_VALUE
)