package com.compose.wechat.main.home.data

import com.compose.wechat.entity.HomeMessage
import kotlinx.coroutines.flow.Flow

interface IHomeMessageRepo {

    fun query(): Flow<List<HomeMessage>>

    suspend fun update(message: HomeMessage): Boolean

    suspend fun delete(message: HomeMessage): Boolean

    suspend fun save(message: HomeMessage): Boolean
}