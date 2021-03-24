package com.study.network.retrofit

sealed class NetworkState<out T> {
    object Init : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
    class Success<out T>(val item: T) : NetworkState<T>()
    class Error(val msg: NetworkMsg) : NetworkState<Nothing>()
    object Empty : NetworkState<Nothing>()
}