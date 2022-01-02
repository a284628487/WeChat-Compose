package com.compose.wechat.ui.main.friends.data

import androidx.room.*
import com.compose.wechat.data.BaseDao
import com.compose.wechat.entity.Friend
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao : BaseDao<Friend> {

    @Query("SELECT * FROM Friend")
    fun query(): Flow<List<Friend>>

    @Query("SELECT * FROM Friend WHERE id=:friendId")
    suspend fun query(friendId: Int): Friend?
}