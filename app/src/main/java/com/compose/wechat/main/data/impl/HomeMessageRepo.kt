package com.compose.wechat.main.data.impl

import com.compose.wechat.main.home.data.HomeMessageDao
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.main.home.data.IHomeMessageRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMessageRepo @Inject constructor(private val homeMessageDao: HomeMessageDao) :
    IHomeMessageRepo {

    override fun query(): Flow<List<HomeMessage>> {
        return homeMessageDao.query()
    }

    override suspend fun update(message: HomeMessage): Boolean {
        return homeMessageDao.update(data = message) > 0
    }

    override suspend fun delete(message: HomeMessage): Boolean {
        return homeMessageDao.delete(data = message) > 0
    }

    override suspend fun save(message: HomeMessage): Boolean {
        return homeMessageDao.save(data = message) > 0
    }
}