package com.compose.wechat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}