package com.compose.wechat.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.wechat.utils.toDateString

@Entity
data class HomeMessage(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val iconUrl: String,
    val title: String,
    val summary: String,
    val senderId: Int,
    val senderName: String,
    val receiverId: Int,
    val receiverName: String,
    val sessionId: Int,
    val date: Long,
    val muted: Boolean = false,
    val isTopped: Boolean = false,
) {

    fun getSessionName(): String {
        return if (sessionId == senderId) {
            senderName
        } else {
            receiverName
        }
    }

    fun getFormatDate(): String {
        return date.toDateString()
    }
}

interface IFriendItem

@Entity
data class Friend(
    @PrimaryKey val id: Int = 0,
    val iconUrl: String,
    val name: String,
    val index: Char,
    val route: String? = null
) : IFriendItem

data class FriendIndexGroup(val name: String) : IFriendItem

data class UiState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val refreshError: Boolean = false
)