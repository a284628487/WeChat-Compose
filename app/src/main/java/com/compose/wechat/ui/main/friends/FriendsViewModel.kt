package com.compose.wechat.ui.main.friends

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.compose.wechat.base.BaseViewModel
import com.compose.wechat.entity.Friend
import com.compose.wechat.entity.FriendIndexGroup
import com.compose.wechat.entity.IFriendItem
import com.compose.wechat.ui.main.friends.data.IFriendsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    application: Application,
    private val repo: IFriendsRepo,
    private val state: SavedStateHandle
) : BaseViewModel(application) {

    fun getFriendsFlow(): Flow<List<IFriendItem>> {
        return repo.query().flatMapConcat { list ->
            val group = list.groupBy {
                it.index
            }
            val indexKeys = group.keys.sorted()
            val iFriendList = mutableListOf<IFriendItem>()
            indexKeys.forEach { indexChar ->
                when (indexChar) {
                    '0' -> {}
                    '1' -> iFriendList.add(FriendIndexGroup("我的企业及企业联系人"))
                    '2' -> iFriendList.add(FriendIndexGroup("星标朋友"))
                    else -> iFriendList.add(FriendIndexGroup("$indexChar"))
                }
                iFriendList.addAll(group[indexChar]!!)
            }
            flowOf(iFriendList)
        }.flowOn(Dispatchers.Default)
    }

    fun update(friend: Friend) {
        viewModelScope.launch {
            val result = repo.update(friend = friend)
        }
    }

    fun delete(friend: Friend) {
        viewModelScope.launch {
            val result = repo.delete(friend = friend)
        }
    }

    fun save(friend: Friend) {
        viewModelScope.launch {
            val result = repo.save(friend = friend)
        }
    }

}