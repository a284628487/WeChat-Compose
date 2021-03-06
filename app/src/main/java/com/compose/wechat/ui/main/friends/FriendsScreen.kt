package com.compose.wechat.ui.main.friends

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.compose.wechat.entity.Friend
import com.compose.wechat.entity.FriendIndexGroup
import com.compose.wechat.entity.IFriendItem
import com.compose.wechat.ui.common.CommonDivider
import com.compose.wechat.ui.main.Router
import com.compose.wechat.ui.common.FunctionalityNotAvailableDialog
import com.compose.wechat.ui.theme.WeChatTheme
import kotlinx.coroutines.launch

object FriendsScreenRouter {
    const val NEW_FRIENDS = "newFriends"
    const val LIMITED_FRIENDS = "limitedFriends"
    const val GROUP_CHAT_LIST = "groupChatList"
    const val LABEL_LIST = "labelList"
    const val OFFICIAL_ACCOUNT = "officialAccount"
    const val COMPANY_WECHAT = "companyWechat"
}

@Composable
fun FriendsScreen(navController: NavHostController, friends: State<List<IFriendItem>>) {
    val showDialog = remember {
        mutableStateOf(false)
    }
    FriendList(
        friendList = friends.value,
        modifier = Modifier
            .fillMaxWidth(),
        content = {
            if (showDialog.value) {
                FunctionalityNotAvailableDialog {
                    showDialog.value = false
                }
            }
        }
    ) {
        it.route?.let {
            showDialog.value = true
        } ?: kotlin.run {
            navController.navigate("${Router.CHAT}/${it.id}/${it.name}")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FriendList(
    friendList: List<IFriendItem>,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
    onFriendItemClicked: (Friend) -> Unit
) {
    Box(modifier = modifier) {
        val listScrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(state = listScrollState) {
            friendList.forEachIndexed { index, iFriendItem ->
                item(key = index) {
                    if (iFriendItem is Friend) {
                        FriendItem(friend = iFriendItem, onFriendItemClicked)
                    } else {
                        FriendIndexGroupItem(group = iFriendItem as FriendIndexGroup)
                    }
                }
            }
        }
        FriendsIndexes(scope = this) { indexString ->
            var index = 0
            if (indexString == "*") {
                // nothing
            } else {
                index = friendList.indexOfFirst {
                    ((it is FriendIndexGroup) && it.name == indexString)
                }
            }
            if (index >= 0) {
                coroutineScope.launch {
                    listScrollState.scrollToItem(index)
                }
            }
        }
        this.content()
    }
}

internal data class IndexTouchIndicate(val y: Float = 0F, val index: String? = null)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FriendsIndexes(scope: BoxScope, onIndexTouched: (String) -> Unit) {
    val density = LocalContext.current.resources.displayMetrics.density
    val singleHeight = 14 * density // (14 * density + 1).toInt()
    val scrollIndicate = remember {
        mutableStateOf(IndexTouchIndicate())
    }
    var touchIndex: String? = null
    scope.apply {
        val indexArray = arrayOf(
            "*", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "#"
        )
        Row(
            modifier = Modifier
                //.height(IntrinsicSize.Min)
                .padding(top = 72.dp)
                .width(68.dp)
                .align(Alignment.CenterEnd)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
            ) {
                scrollIndicate.value.index?.let {
                    if (touchIndex != it) {
                        onIndexTouched(it)
                    }
                    Spacer(
                        modifier = Modifier
                            .height((scrollIndicate.value.y / density + 6).dp)
                    )
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 1.dp)
                            .width(32.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colors.secondary),
                        color = MaterialTheme.colors.onSecondary,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                }
                touchIndex = scrollIndicate.value.index
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .width(20.dp)
                    .pointerInteropFilter {
                        if (it.action == MotionEvent.ACTION_CANCEL || it.action == MotionEvent.ACTION_UP) {
                            scrollIndicate.value = IndexTouchIndicate()
                        } else {
                            var index = (it.y / singleHeight).toInt()
                            var y = it.y
                            if (index >= indexArray.size) {
                                index = indexArray.size - 1
                                y = (index + 1) * singleHeight
                            }
                            if (index < 0) {
                                index = 0
                            }
                            scrollIndicate.value = IndexTouchIndicate(y, indexArray[index])
                        }
                        true
                    }
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.Center)
                ) {
                    indexArray.forEachIndexed { index, s ->
                        Text(
                            text = s,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp),
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 8.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FriendItem(friend: Friend, onClick: (Friend) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clickable(enabled = true, onClick = {
                    onClick(friend)
                })
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = friend.iconUrl),
                contentDescription = "",
                modifier = Modifier
                    .size(width = 36.dp, height = 36.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Text(
                text = friend.name,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 12.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        }
        CommonDivider(modifier = Modifier.offset(x = 64.dp))
    }
}

@Composable
fun FriendIndexGroupItem(group: FriendIndexGroup) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(Color(0xFFEDEDED)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = group.name,
            modifier = Modifier.padding(start = 10.dp),
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FriendIndexGroupItemPreview() {
    WeChatTheme {
        FriendIndexGroupItem(FriendIndexGroup("????????????"))
    }
}

@Preview(showBackground = true)
@Composable
fun FriendItemPreview() {
    WeChatTheme {
        FriendItem(Friend(1, "", "??????", '0'), {})
    }
}
