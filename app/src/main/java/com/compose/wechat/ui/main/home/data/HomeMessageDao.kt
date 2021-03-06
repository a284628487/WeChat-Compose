package com.compose.wechat.ui.main.home.data

import androidx.room.*
import com.compose.wechat.data.BaseDao
import com.compose.wechat.entity.HomeMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeMessageDao : BaseDao<HomeMessage> {

    @Query("SELECT * FROM HomeMessage GROUP BY sessionId having max(date) ORDER BY date DESC")
    fun query(): Flow<List<HomeMessage>>

    @Query("SELECT * FROM HomeMessage WHERE sessionId=:friendId ORDER BY date DESC")
    fun query(friendId: Int): Flow<List<HomeMessage>>
}
