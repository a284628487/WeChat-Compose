package com.compose.wechat.main.moments.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.compose.wechat.R
import com.compose.wechat.ui.theme.WeChatTheme

class MomentsFragment : Fragment() {

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // In order for savedState to work, the same ID needs to be used for all instances.
            id = R.id.main_home_fragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                WeChatTheme {
                    // actual composable state
                    val nativePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        textSize = 36f
                        color = android.graphics.Color.RED
                    }
                }
            }
        }
    }
}