package com.compose.wechat.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    init {
        Log.d(this.javaClass.simpleName, "init")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(this.javaClass.simpleName, "onCleared")
    }
}