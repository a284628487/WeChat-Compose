package com.compose.wechat.ui.main.chat

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.compose.wechat.base.BaseViewModel
import com.compose.wechat.data.FakeHeads
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.ui.main.chat.data.IChatRepo
import com.compose.wechat.utils.logd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    private val repo: IChatRepo,
    state: SavedStateHandle
) : BaseViewModel(application) {

    private var sessionId: Int = 0
    private var sessionName: String = ""
    private var sessionIcon: String = ""

    private val myName: String = "God"
    private val myId: Int = Int.MAX_VALUE / 1000

    init {
        val id = state.get<Int>("id") ?: 0
        val name = state.get<String>("name") ?: ""
        logd<ChatViewModel>("id: ${id}, name: ${name}")
        if (id != 0) {
            sessionId = id
            sessionName = name
            viewModelScope.launch {
                sessionIcon = repo.getSessionUserIcon(id)
            }
        }
    }

    fun setup(id: Int, name: String) {
        this.sessionId = id
        this.sessionName = name
    }

    fun getMessages(): Flow<List<HomeMessage>> {
        return repo.query(sessionId)
    }

    fun getSessionName(): String {
        return sessionName
    }

    fun saveReceivedMessage(message: String) {
        GlobalScope.launch {
            repo.save(
                HomeMessage(
                    id = 0,
                    title = sessionName,
                    summary = message,
                    senderId = sessionId,
                    senderName = sessionName,
                    senderIcon = sessionIcon,
                    receiverId = myId,
                    receiverName = myName,
                    sessionId = sessionId,
                    sessionIcon = sessionIcon,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun saveSendMessage(message: String) {
        GlobalScope.launch {
            repo.save(
                HomeMessage(
                    id = 0,
                    title = sessionName,
                    summary = message,
                    senderId = myId,
                    senderName = myName,
                    senderIcon = FakeHeads.MyHeadIcon,
                    receiverId = sessionId,
                    receiverName = sessionName,
                    sessionId = sessionId,
                    sessionIcon = sessionIcon,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun removeMessage(message: HomeMessage) {
        GlobalScope.launch {
            repo.delete(message)
        }
    }
}