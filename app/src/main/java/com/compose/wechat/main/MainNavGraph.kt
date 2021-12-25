package com.compose.wechat.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.compose.wechat.main.chat.ui.ChatList
import com.compose.wechat.main.chat.vm.ChatViewModel
import com.compose.wechat.main.friends.ui.FriendList
import com.compose.wechat.main.friends.vm.FriendsViewModel
import com.compose.wechat.main.home.ui.EmptyView
import com.compose.wechat.main.home.ui.HomeMessageList
import com.compose.wechat.main.home.vm.HomeViewModel
import com.compose.wechat.ui.theme.WeChatTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    NavHost(navController = navController, startDestination = Router.LAUNCH) {
        Log.d("NavHost", "compose")
        composable(Router.MAIN) {
            Log.d("Main", "compose")
            MainPage(navController, indexState)
        }
        composable(Router.LAUNCH) {
            Log.d("Launch", "compose")
            Button(onClick = { navController.navigate(Router.MAIN) }) {
                Text(text = "Go to Main")
            }
        }
        composable(
            "${Router.CHAT}/{id}/{name}",
            arguments = listOf(navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getInt("id")
            val name = it.arguments?.getString("name")
            Log.d("Chat", "compose")

            val chatViewModel = hiltViewModel<ChatViewModel>()
            chatViewModel.setup(id ?: 0, name ?: "")
            val list = chatViewModel.getMessages().collectAsState(initial = emptyList())
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

data class NavItem(val name: String, val route: String, val resId: Int)

@Composable
fun MainPage(navController: NavHostController, indexState: MutableState<Int>) {
    val navList = mutableListOf(
        NavItem(stringResource(id = R.string.main_home), Router.MESSAGE, R.drawable.ic_main_home),
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
    Scaffold(
        bottomBar = {
            BottomBar(navList = navList, indexState)
        }
    ) {
        Log.d("MainPage", "index = ${indexState.value}")
        if (indexState.value == 0) {
            Log.d("Message", "compose")
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val messages = homeViewModel.getMessagesFlow().collectAsState(initial = emptyList())
            HomeMessageList(
                messageList = messages.value
            ) {
                navController.navigate("${Router.CHAT}/${it.sessionId}/${it.getSessionName()}")
            }
            if (messages.value.isEmpty()) {
                EmptyView()
            }
        } else if (indexState.value == 1) {
            Log.d("Friends", "compose")
            val friendsViewModel = hiltViewModel<FriendsViewModel>()
            val friends = friendsViewModel.getFriendsFlow().collectAsState(emptyList())
            FriendList(friendList = friends.value) {
                navController.navigate("${Router.CHAT}/${it.id}/${it.name}")
            }
        } else if (indexState.value == 2) {
            Log.d("Moments", "compose")
            Text(text = "22222", modifier = Modifier.padding(10.dp))
        } else {
            Log.d("Profile", "compose")
            Text(text = "33333", modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun BottomBar(navList: List<NavItem>, selectedIndex: MutableState<Int>) {
    BottomNavigation(
        elevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colors.primary)
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
        MainPage(rememberNavController(), remember {
            mutableStateOf(0)
        })
    }
}
