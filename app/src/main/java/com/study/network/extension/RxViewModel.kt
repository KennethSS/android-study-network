package com.study.network.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class RxViewModel : ViewModel() {
    private val dispose = CompositeDisposable()

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    override fun onCleared() {
        super.onCleared()
        dispose.clear()
    }
}