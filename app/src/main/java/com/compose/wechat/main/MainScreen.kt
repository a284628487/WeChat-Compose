package com.compose.wechat.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.compose.wechat.entity.UiState
import com.compose.wechat.main.friends.ui.FriendsScreen
import com.compose.wechat.main.friends.vm.FriendsViewModel
import com.compose.wechat.main.home.ui.HomeScreen
import com.compose.wechat.main.home.vm.HomeViewModel
import com.compose.wechat.main.moments.ui.MomentsList
import com.compose.wechat.main.moments.ui.MomentsScreen
import com.compose.wechat.main.moments.vm.MomentsViewModel
import com.compose.wechat.main.profile.ui.ProfileScreen
import com.compose.wechat.main.profile.vm.ProfileViewModel
import com.compose.wechat.ui.common.BackPressHandler
import com.compose.wechat.ui.theme.WeChatTheme
import com.compose.wechat.utils.onPress
import com.compose.wechat.utils.touchSwitchState

data class NavItem(val name: String, val route: String, val resId: Int)

@Composable
fun MainScreen(
    navController: NavHostController,
    navList: List<NavItem>,
    indexState: MutableState<Int>,
    onMainSearchClick: () -> Unit = {}
) {
    val showAddPanelState = remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.onPress {
            showAddPanelState.value = false
        },
        topBar = {
            if (indexState.value != 3) {
                TopAppBar(title = {
                    Text(text = navList[indexState.value].name)
                }, modifier = Modifier.touchSwitchState(showAddPanelState), actions = {
                    IconButton(onClick = onMainSearchClick) {
                        Icon(Icons.Filled.Search, null, tint = MaterialTheme.colors.onPrimary)
                    }
                    IconButton(onClick = {
                        showAddPanelState.value = true
                    }) {
                        Icon(Icons.Filled.Add, null, tint = MaterialTheme.colors.onPrimary)
                    }
                })
            }
        },
        bottomBar = {
            MainBottomBar(navList = navList, indexState, showAddPanelState)
        }
    ) {
        Log.d("MainPage", "compose = ${indexState.value}")
        Box(modifier = Modifier.touchSwitchState(showAddPanelState)) {
            if (indexState.value == 0) {
                Log.d("Message", "compose")
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val uiState =
                    homeViewModel.getMessagesFlow().collectAsState(UiState(loading = true))
                HomeScreen(
                    uiState = uiState.value
                ) {
                    navController.navigate("${Router.CHAT}/${it.sessionId}/${it.getSessionName()}")
                }
            } else if (indexState.value == 1) {
                Log.d("Friends", "compose")
                val friendsViewModel = hiltViewModel<FriendsViewModel>()
                val friends = friendsViewModel.getFriendsFlow().collectAsState(emptyList())
                FriendsScreen(navController, friends)
            } else if (indexState.value == 2) {
                Log.d("Moments", "compose")
                val momentsViewModel = hiltViewModel<MomentsViewModel>()
                val configs = momentsViewModel.getMomentConfigs()
                MomentsScreen(configs)
            } else {
                Log.d("Profile", "compose")
                val momentsViewModel = hiltViewModel<ProfileViewModel>()
                val menus = momentsViewModel.getProfileMenuList()
                val user = momentsViewModel.getUser()
                ProfileScreen(user, menus) {
                }
            }
            //
            if (showAddPanelState.value) {
                // show add panel at right-top
                AddPanel(showAddPanelState)
                // press back key will dismiss the add panel
                BackPressHandler {
                    showAddPanelState.value = false
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainBottomBar(
    navList: List<NavItem>,
    selectedIndex: MutableState<Int>,
    addPanelShowState: MutableState<Boolean>,
) {
    BottomNavigation(
        elevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colors.primary)
            .touchSwitchState(addPanelShowState)
    ) {
        navList.forEachIndexed { index, it ->
            BottomNavigationItem(
                selected = selectedIndex.value == index,
                onClick = {
                    selectedIndex.value = index
                },
                icon = {
                    Icon(
                        painterResource(id = it.resId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                label = {
                    Text(
                        it.name,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun MainPagePreview() {
    WeChatTheme() {
        MainScreen(rememberNavController(), emptyList(), remember {
            mutableStateOf(0)
        })
    }
}

