package com.compose.wechat.main.friends.data

import androidx.room.*
import com.compose.wechat.data.BaseDao
import com.compose.wechat.entity.Friend
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao: BaseDao<Friend> {

    @Query("SELECT * FROM Friend")
    fun query(): Flow<List<Friend>>

}