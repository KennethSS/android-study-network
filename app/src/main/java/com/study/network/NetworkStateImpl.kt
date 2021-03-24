package com.study.network

import android.content.Context
import com.study.network.helper.NetworkHelper
import com.study.network.retrofit.interceptor.NetworkState

class NetworkStateImpl(context: Context) : NetworkState {

    private val networkHelper: NetworkHelper by lazy { NetworkHelper(context) }

    override fun isNetworkConnected(): Boolean {
        return networkHelper.isNetworkConnected()
    }
}