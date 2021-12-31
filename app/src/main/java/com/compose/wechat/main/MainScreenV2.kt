package com.compose.wechat.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.compose.wechat.entity.UiState
import com.compose.wechat.main.friends.ui.FriendsScreen
import com.compose.wechat.main.friends.vm.FriendsViewModel
import com.compose.wechat.main.home.ui.HomeScreen
import com.compose.wechat.main.home.vm.HomeViewModel
import com.compose.wechat.main.moments.ui.MomentsScreen
import com.compose.wechat.main.moments.vm.MomentsViewModel
import com.compose.wechat.main.profile.ui.ProfileScreen
import com.compose.wechat.main.profile.vm.ProfileViewModel
import com.compose.wechat.ui.common.BackPressHandler
import com.compose.wechat.utils.onPress
import com.compose.wechat.utils.touchSwitchState


@Composable
fun NavWithBottomNavigation(
    parentNavController: NavHostController,
    navList: List<NavItem>,
    onMainSearchClick: () -> Unit = {}
) {
    val showAddPanelState = remember {
        mutableStateOf(false)
    }
    val navController = rememberNavController()
    val state = navController.currentBackStackEntryAsState()
    Scaffold(
        modifier = Modifier.onPress {
            showAddPanelState.value = false
        },
        topBar = {
            val desRoute = state.value?.destination?.route
            val desName = navList.find {
                it.route == desRoute
            }?.name ?: return@Scaffold
            if (desRoute != Router.PROFILE) {
                TopAppBar(title = {
                    Text(text = desName)
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
            BottomNavigation(modifier = Modifier.touchSwitchState(showAddPanelState)) {
                val currentDestination = state.value?.destination
                navList.forEach {
                    BottomNavigationItem(
                        selected = currentDestination?.route == it.route,
                        onClick = {
                            navController.navigate(it.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reSelecting the same item
                                launchSingleTop = true
                                // Restore state when reSelecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(id = it.resId),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        label = {
                            Text(it.name)
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Router.MESSAGE,
            modifier = Modifier
                .padding(it)
                .touchSwitchState(showAddPanelState)
        ) {
            composable(Router.MESSAGE) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val uiState =
                    homeViewModel.getMessagesFlow().collectAsState(UiState(loading = true))
                HomeScreen(
                    uiState = uiState.value
                ) {
                    parentNavController.navigate("${Router.CHAT}/${it.sessionId}/${it.getSessionName()}")
                }
            }
            composable(Router.FRIENDS) {
                val friendsViewModel = hiltViewModel<FriendsViewModel>()
                val friends = friendsViewModel.getFriendsFlow().collectAsState(emptyList())
                FriendsScreen(parentNavController, friends)
            }
            composable(Router.MOMENTS) {
                val momentsViewModel = hiltViewModel<MomentsViewModel>()
                val configs = momentsViewModel.getMomentConfigs()
                MomentsScreen(configs)
            }
            composable(Router.PROFILE) {
                val momentsViewModel = hiltViewModel<ProfileViewModel>()
                val menus = momentsViewModel.getProfileMenuList()
                val user = momentsViewModel.getUser()
                ProfileScreen(user, menus) {
                }
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
