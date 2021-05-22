package com.study.network.http

sealed class NetworkState<out T> {
    class Success<out T>(val item: T) : NetworkState<T>()
    class Error(val throwable: Throwable?) : NetworkState<Nothing>()
}