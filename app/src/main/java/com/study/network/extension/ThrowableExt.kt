package com.study.network

import com.study.network.retrofit.exception.NetworkConnectException

/**
 * Copyright 2020 Kenneth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/
fun Throwable.toNetworkMsg(): NetworkMsg {
    return when(this) {
        is NetworkConnectException -> NetworkMsg(
            desc = "네트워크가 연결되지 않았습니다. 연결 상태 확인 후 다시 시도해주세요.")
        else -> {
            // Firebase crash
            NetworkMsg(
                "알림", "예상치 못한 문제가 발생했습니다. 잠시 후 다시 시도해주세요.")
        }
    }
}