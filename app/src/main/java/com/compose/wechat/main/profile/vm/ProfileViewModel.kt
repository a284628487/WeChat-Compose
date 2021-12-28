package com.compose.wechat.main.profile.vm

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.compose.wechat.base.BaseViewModel
import com.compose.wechat.entity.JumpConfig
import com.compose.wechat.entity.JumpGroup
import com.compose.wechat.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(application: Application) :
    BaseViewModel(application = application) {

    private val list = mutableListOf<JumpGroup>()

    private val user = User("cj758661", "Ccflying88")

    init {
        list.add(
            JumpGroup(
                listOf(
                    JumpConfig(
                        Icons.Filled.Payment,
                        "支付",
                        ""
                    )
                )
            )
        )
        list.add(
            JumpGroup(
                listOf(
                    JumpConfig(Icons.Filled.Star, "收藏", ""),
                    JumpConfig(Icons.Filled.AccountCircle, "朋友圈", ""),
                    JumpConfig(Icons.Filled.CardMembership, "卡包", ""),
                    JumpConfig(Icons.Filled.EmojiEmotions, "表情", "")
                )
            )
        )
        list.add(
            JumpGroup(
                listOf(
                    JumpConfig(Icons.Filled.Settings, "设置", ""),
                )
            )
        )
    }

    fun getProfileMenuList() = list

    fun getUser() = user
}