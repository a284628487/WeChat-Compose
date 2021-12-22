package com.compose.wechat.main.data.di

import com.compose.wechat.main.chat.data.IChatRepo
import com.compose.wechat.main.data.impl.ChatRepo
import com.compose.wechat.main.friends.data.IFriendsRepo
import com.compose.wechat.main.home.data.IHomeMessageRepo
import com.compose.wechat.main.data.impl.FriendsRepo
import com.compose.wechat.main.data.impl.HomeMessageRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ModuleInject {

    @Binds
    abstract fun bindHomeMessageRepo(repo: HomeMessageRepo): IHomeMessageRepo

    @Binds
    abstract fun bindFriendsRepo(repo: FriendsRepo): IFriendsRepo

    @Binds
    abstract fun bindChatRepo(repo: ChatRepo): IChatRepo

}