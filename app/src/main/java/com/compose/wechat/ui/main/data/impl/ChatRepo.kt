package com.compose.wechat.ui.main.data.impl

import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.ui.main.chat.data.IChatRepo
import com.compose.wechat.ui.main.home.data.HomeMessageDao
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ChatRepo @Inject constructor(private val dao: HomeMessageDao) : IChatRepo {

    override fun query(friendId: Int): Flow<List<HomeMessage>> {
        return dao.query(friendId)
    }

    override suspend fun delete(message: HomeMessage): Boolean {
        return dao.delete(message) > 0
    }

    override suspend fun save(message: HomeMessage): Boolean {
        return dao.save(message) > 0
    }
}