package com.compose.wechat.main.home.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.compose.wechat.base.BaseViewModel
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.main.home.data.IHomeMessageRepo
import com.compose.wechat.utils.logd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repo: IHomeMessageRepo,
    private val state: SavedStateHandle
) : BaseViewModel(application) {

    fun getMessagesFlow(): Flow<List<HomeMessage>> {
        return repo.query()
    }

    fun update(message: HomeMessage) {
        viewModelScope.launch {
            val result = repo.update(message = message)
        }
    }

    fun delete(message: HomeMessage) {
        viewModelScope.launch {
            val result = repo.delete(message = message)
        }
    }

    fun save(message: HomeMessage) {
        viewModelScope.launch {
            val result = repo.save(message = message)
        }
    }

}