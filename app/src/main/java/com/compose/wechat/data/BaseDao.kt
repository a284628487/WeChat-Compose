package com.compose.wechat.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T>{

    @Update
    suspend fun update(data: T): Int

    @Delete
    suspend fun delete(data: T): Int

    @Insert
    suspend fun save(data: T): Long

    @Insert
    suspend fun saveAll(list: List<T>)

}