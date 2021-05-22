package com.study.network.http

data class Data(
    var direct_discount_price: Int? = null,
    var id: Int? = null,
    var price: Int? = null,
    var remain_count: Int? = null,
    var require_reservation: Boolean? = null,
    var required_check: String? = null,
    var support_price: Int? = null,
    var support_price_rate: Int? = null,
    var title: String? = null,
    var use_today: Boolean? = null,
    var use_weekday: Boolean? = null,
    var use_weekend: Boolean? = null
)