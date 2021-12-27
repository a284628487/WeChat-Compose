package com.compose.wechat.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.compose.wechat.R
import com.compose.wechat.entity.UiState
import com.compose.wechat.main.chat.ui.ChatList
import com.compose.wechat.main.chat.vm.ChatViewModel
import com.compose.wechat.main.friends.ui.FriendList
import com.compose.wechat.main.friends.vm.FriendsViewModel
import com.compose.wechat.main.home.ui.HomeMessageList
import com.compose.wechat.main.home.vm.HomeViewModel
import com.compose.wechat.ui.theme.WeChatTheme
import com.compose.wechat.ui.theme.isLaunchScreenShowed
import com.compose.wechat.utils.touchSwitchState

object Router {
    const val LAUNCH = "launch"
    const val MAIN = "main"
    const val MESSAGE = "message"
    const val FRIENDS = "friends"
    const val MOMENTS = "moments"
    const val PROFILE = "profile"
    const val CHAT = "chat"
}

@Composable
fun MainNavGraph() {
    Log.d("MainNavGraph", "compose")
    val navController = rememberNavController()

    val indexState = remember {
        mutableStateOf(0)
    }

    NavHost(navController = navController, startDestination = Router.MAIN) {
        Log.d("NavHost", "compose")
        composable(Router.MAIN) {
            if (isLaunchScreenShowed) {
                Log.d("Main", "compose")
                val navList = mutableListOf(
                    NavItem(
                        stringResource(id = R.string.main_home),
                        Router.MESSAGE,
                        R.drawable.ic_main_home
                    ),
                    NavItem(
                        stringResource(id = R.string.main_friends),
                        Router.FRIENDS,
                        R.drawable.ic_main_friends
                    ),
                    NavItem(
                        stringResource(id = R.string.main_moments),
                        Router.MOMENTS,
                        R.drawable.ic_main_moments
                    ),
                    NavItem(
                        stringResource(id = R.string.main_profile),
                        Router.PROFILE,
                        R.drawable.ic_main_profile
                    )
                )
                MainPage(navController, navList, indexState)
            } else {
                navController.navigate(Router.LAUNCH)
            }
        }
        composable(Router.LAUNCH) {
            Log.d("Launch", "compose")
            LaunchScreen(navController = navController)
        }
        composable(
            "${Router.CHAT}/{id}/{name}",
            arguments = listOf(navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType })
        ) {
            Log.d("Chat", "compose")
            val id = it.arguments?.getInt("id")
            val name = it.arguments?.getString("name")

            val chatViewModel = hiltViewModel<ChatViewModel>()
            val list = chatViewModel.getMessages().collectAsState(initial = emptyList())
            Scaffold(topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(MaterialTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chatViewModel.getSessionName(),
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                }
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, null, tint = MaterialTheme.colors.onPrimary)
                }
            }) {
                ChatList(list = list.value, {
                    chatViewModel.removeMessage(it)
                }, {
                    if (it.isEmpty().not()) {
                        chatViewModel.saveSendMessage(it)
                    }
                })
            }
        }
    }
}

data class NavItem(val name: String, val route: String, val resId: Int)

@Composable
fun MainPage(
    navController: NavHostController,
    navList: List<NavItem>,
    indexState: MutableState<Int>,
    onMainSearchClick: () -> Unit = {}
) {
    val showAddPanelState = remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onPress = {
                showAddPanelState.value = false
            })
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
            BottomBar(navList = navList, indexState, showAddPanelState)
        }
    ) {
        Log.d("MainPage", "compose = ${indexState.value}")
        Box(modifier = Modifier.touchSwitchState(showAddPanelState)) {
            if (indexState.value == 0) {
                Log.d("Message", "compose")
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val uiState =
                    homeViewModel.getMessagesFlow().collectAsState(UiState(loading = true))
                HomeMessageList(
                    uiState = uiState.value
                ) {
                    navController.navigate("${Router.CHAT}/${it.sessionId}/${it.getSessionName()}")
                }
            } else if (indexState.value == 1) {
                Log.d("Friends", "compose")
                val friendsViewModel = hiltViewModel<FriendsViewModel>()
                val friends = friendsViewModel.getFriendsFlow().collectAsState(emptyList())
                FriendList(
                    friendList = friends.value,
                    modifier = Modifier.padding(bottom = 56.dp)
                ) {
                    navController.navigate("${Router.CHAT}/${it.id}/${it.name}")
                }
            } else if (indexState.value == 2) {
                Log.d("Moments", "compose")
                Text(text = "22222", modifier = Modifier.padding(10.dp))
            } else {
                Log.d("Profile", "compose")
                Text(text = "33333", modifier = Modifier.padding(10.dp))
            }
            //
            if (showAddPanelState.value) {
                AddPanel(showAddPanelState)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomBar(
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
        MainPage(rememberNavController(), emptyList(), remember {
            mutableStateOf(0)
        })
    }
}
