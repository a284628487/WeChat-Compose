package com.compose.wechat.ui.main.data.impl

import com.compose.wechat.ui.main.friends.data.FriendsDao
import com.compose.wechat.entity.Friend
import com.compose.wechat.ui.main.friends.data.IFriendsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepo @Inject constructor(private val friendsDao: FriendsDao) : IFriendsRepo {

    override fun query(): Flow<List<Friend>> {
        return friendsDao.query()
    }

    override suspend fun update(friend: Friend): Boolean {
        return friendsDao.update(data = friend) > 0
    }

    override suspend fun delete(friend: Friend): Boolean {
        return friendsDao.delete(data = friend) > 0
    }

    override suspend fun save(friend: Friend): Boolean {
        return friendsDao.save(data = friend) > 0
    }

}