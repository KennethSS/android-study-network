package com.study.network

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
/*
fun <T> Single<T>.singleNetwork(state: MutableLiveData<NetworkState<T>>) =
    subscribeOn(Schedulers.io())
        .doOnSubscribe { state.postValue(NetworkState.Loading) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { state.postValue(NetworkState.Success(it))  },
            { state.postValue(NetworkState.Error(it.toNetworkMsg())) }
        )*/
