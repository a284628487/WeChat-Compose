package com.compose.wechat.main.chat.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.main.chat.data.IChatRepo
import com.compose.wechat.main.data.impl.ChatRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(application: Application, private val repo: IChatRepo) :
    AndroidViewModel(application) {

    private var senderName: String = ""
    private var senderId: Int = 0

    private val myName: String = "God"
    private val myId: Int = Int.MAX_VALUE / 1000

    fun setup(id: Int, name: String) {
        this.senderId = id
        this.senderName = name
    }

    fun getMessages(): Flow<List<HomeMessage>> {
        return repo.query(senderId)
    }

    fun saveReceivedMessage(message: String) {
        GlobalScope.launch {
            repo.save(
                HomeMessage(
                    0,
                    "",
                    senderName,
                    message,
                    senderId,
                    senderName,
                    myId,
                    myName,
                    senderId,
                    System.currentTimeMillis()
                )
            )
        }
    }

    fun saveSendMessage(message: String) {
        GlobalScope.launch {
            repo.save(
                HomeMessage(
                    0,
                    "",
                    senderName,
                    message,
                    myId,
                    myName,
                    senderId,
                    senderName,
                    senderId,
                    System.currentTimeMillis()
                )
            )
        }
    }
}