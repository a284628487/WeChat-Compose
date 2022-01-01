package com.compose.wechat.data.di

import android.content.Context
import com.compose.wechat.ui.main.friends.data.FriendsDao
import com.compose.wechat.ui.main.home.data.HomeMessageDao
import com.compose.wechat.data.WeChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object DatabaseInject {

    @Provides
    fun getDatabase(@ApplicationContext context: Context) =
        WeChatDatabase.getInstance(context, CoroutineScope(SupervisorJob()))

    @Provides
    fun provideHomeMessageDao(database: WeChatDatabase): HomeMessageDao {
        return database.homeMessageDao()
    }

    @Provides
    fun provideFriendsDao(database: WeChatDatabase): FriendsDao {
        return database.friendsDao()
    }
}