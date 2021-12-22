package com.compose.wechat.main.chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.compose.wechat.R
import com.compose.wechat.entity.HomeMessage

@Composable
fun ChatList(list: List<HomeMessage>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        list.forEachIndexed { index, message ->
            item(key = index) {
                if (message.senderId == message.sessionId) {
                    ChatMessageReceived(message = message)
                } else {
                    ChatMessageSend(message = message)
                }
            }
        }
    }
}

@Composable
fun ChatMessageReceived(message: HomeMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 56.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_frag),
            contentDescription = "",
            modifier = Modifier
                .size(36.dp, 36.dp)
                .clip(MaterialTheme.shapes.medium),
        )
        Text(
            text = message.summary, modifier = Modifier
                .offset(x = 8.dp)
                .background(Color.Cyan)
                .wrapContentSize()
                .padding(8.dp)
        )
    }
}

@Composable
fun ChatMessageSend(message: HomeMessage) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 56.dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
    ) {
        val (messageText, spacer, icon) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.ic_frag),
            contentDescription = "",
            modifier = Modifier
                .size(36.dp, 36.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
        )
        Spacer(modifier = Modifier
            .width(8.dp)
            .constrainAs(spacer) {
                top.linkTo(parent.top)
                end.linkTo(icon.start)
            })
        Text(
            text = message.summary, modifier = Modifier
                .background(Color.Cyan)
                .wrapContentWidth(Alignment.End)
                .padding(8.dp)
                .constrainAs(messageText) {
                    top.linkTo(parent.top)
                    end.linkTo(spacer.start)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageReceivedPreview() {
    ChatMessageReceived(
        HomeMessage(
            0,
            "",
            "Hello",
            "Hello ni hao",
            0,
            "FriendA",
            0,
            "Me",
            0,
            System.currentTimeMillis()
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ChatMessageSendPreview() {
    ChatMessageSend(
        HomeMessage(
            0,
            "",
            "Hello",
            "Hello ni hao",
            0,
            "FriendA",
            0,
            "Me",
            0,
            System.currentTimeMillis()
        )
    )
}