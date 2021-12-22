package com.compose.wechat.main.chat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.compose.wechat.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val chatViewModel by viewModels<ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        chatViewModel.setup(
            arguments?.getInt("friend_id") ?: 0,
            arguments?.getString("friend_name", "") ?: ""
        )
        return ComposeView(requireContext()).apply {
            id = R.id.main_chat_fragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                val list = chatViewModel.getMessages().collectAsState(initial = emptyList())
                ChatList(list = list.value)
            }
        }
    }

}