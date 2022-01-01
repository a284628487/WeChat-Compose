package com.compose.wechat.ui.main.chat.data

import androidx.annotation.WorkerThread
import com.compose.wechat.entity.HomeMessage
import kotlinx.coroutines.flow.Flow

interface IChatRepo {

    fun query(friendId: Int): Flow<List<HomeMessage>>

    @WorkerThread
    suspend fun delete(message: HomeMessage): Boolean

    @WorkerThread
    suspend fun save(message: HomeMessage): Boolean
}