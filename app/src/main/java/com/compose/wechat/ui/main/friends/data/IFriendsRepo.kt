package com.compose.wechat.ui.main.friends.data

import androidx.annotation.WorkerThread
import com.compose.wechat.entity.Friend
import kotlinx.coroutines.flow.Flow

interface IFriendsRepo {

    fun query(): Flow<List<Friend>>

    @WorkerThread
    suspend fun update(friend: Friend): Boolean

    @WorkerThread
    suspend fun delete(friend: Friend): Boolean

    @WorkerThread
    suspend fun save(friend: Friend): Boolean
}