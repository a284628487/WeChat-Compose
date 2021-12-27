package com.compose.wechat.main.friends.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.wechat.R
import com.compose.wechat.entity.Friend
import com.compose.wechat.entity.FriendIndexGroup
import com.compose.wechat.entity.IFriendItem
import com.compose.wechat.ui.theme.WeChatTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FriendList(
    friendList: List<IFriendItem>,
    modifier: Modifier = Modifier,
    onFriendItemClicked: (Friend) -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn() {
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
        val indexArray = arrayOf(
            "*",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z",
            "#"
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(20.dp)
                .pointerInteropFilter {
                    // TODO
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
                            .padding(vertical = 1.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 8.sp)
                    )
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_frag),
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
        Divider(
            modifier = Modifier
                .offset(x = 64.dp),
            color = Color.LightGray,
            thickness = 0.3.dp
        )
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
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FriendIndexGroupItemPreview() {
    WeChatTheme {
        FriendIndexGroupItem(FriendIndexGroup("星标朋友"))
    }
}

@Preview(showBackground = true)
@Composable
fun FriendItemPreview() {
    WeChatTheme {
        FriendItem(Friend(1, "", "标签", '0'), {})
    }
}
