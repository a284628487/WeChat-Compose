package com.compose.wechat.main.home.data

import androidx.room.*
import com.compose.wechat.data.BaseDao
import com.compose.wechat.entity.HomeMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeMessageDao: BaseDao<HomeMessage> {

    @Query("SELECT * FROM HomeMessage")
    fun query(): Flow<List<HomeMessage>>

}