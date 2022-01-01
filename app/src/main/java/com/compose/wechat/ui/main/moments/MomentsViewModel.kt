package com.compose.wechat.ui.main.moments

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.compose.wechat.R
import com.compose.wechat.base.BaseViewModel
import com.compose.wechat.entity.JumpConfig
import com.compose.wechat.entity.JumpGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MomentsViewModel @Inject constructor(application: Application) :
    BaseViewModel(application = application) {

    private val list = mutableListOf<JumpGroup>()

    init {
        list.add(
            JumpGroup(
                listOf(
                    JumpConfig(
                        Icons.Filled.BlurCircular,
                        "朋友圈",
                        "",
                        listOf(R.drawable.ic_frag)
                    )
                )
            )
        )
        list.add(
            JumpGroup(
                listOf(
                    JumpConfig(
                        Icons.Filled.VideoLabel,
                        "视频号",
                        "",
                        listOf(R.drawable.ic_frag, "赞过")
                    ),
                    JumpConfig(
                        Icons.Filled.LiveTv,
                        "直播",
                        "",
                        listOf("朋友正在看的直播", R.drawable.ic_frag)
                    )
                )
            )
        )
        list.add(
            JumpGroup(
                listOf(
                    JumpConfig(Icons.Filled.QrCodeScanner, "扫一扫", ""),
                    JumpConfig(Icons.Filled.FactCheck, "摇一摇", "")
                )
            )
        )
        list.add(
            JumpGroup(
                listOf(
                    JumpConfig(Icons.Filled.LocalSee, "看一看", ""),
                    JumpConfig(Icons.Filled.Search, "搜一搜", "")
                )
            )
        )
    }

    fun getMomentConfigs() = list
}