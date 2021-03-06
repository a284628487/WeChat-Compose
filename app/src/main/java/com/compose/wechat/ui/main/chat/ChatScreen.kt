package com.compose.wechat.ui.main.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.compose.wechat.R
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.ui.common.CommonTopBar
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(navController: NavHostController) {
    val chatViewModel = hiltViewModel<ChatViewModel>()
    val list = chatViewModel.getMessages().collectAsState(initial = emptyList())
    Scaffold(topBar = {
        CommonTopBar(navController = navController, title = chatViewModel.getSessionName())
    }) {
        Log.d("ChatScreen", "compose")
        ChatList(list = list.value, {
            chatViewModel.removeMessage(it)
        }, {
            if (it.isEmpty().not()) {
                chatViewModel.saveSendMessage(it)
            }
        }, {
            Log.d("ChatScreen", "check empty")
            if (list.value.isEmpty()) {
                chatViewModel.saveReceivedMessage("How are you?")
            }
        })
    }
}

@Composable
fun ChatList(
    list: List<HomeMessage>,
    onLongPress: (HomeMessage) -> Unit,
    onSendClick: (String) -> Unit,
    checkEmpty: () -> Unit = {}
) {
    val innerCheckEmpty by rememberUpdatedState(checkEmpty)
    LaunchedEffect(key1 = Unit, block = {
        delay(1200)
        innerCheckEmpty()
    })
    Column {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.background)
        ) {
            LazyColumn(reverseLayout = true) {
                list.forEachIndexed { index, message ->
                    item(key = index) {
                        if (message.senderId == message.sessionId) {
                            ChatMessageReceived(message = message, onLongPress = onLongPress)
                        } else {
                            ChatMessageSend(message = message, onLongPress = onLongPress)
                        }
                    }
                }
            }
        }
        SendMessageInput(
            modifier = Modifier
                .fillMaxWidth(),
            onSendClick = onSendClick
        )
    }
}

@Composable
fun ChatMessageReceived(message: HomeMessage, onLongPress: (HomeMessage) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 56.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = message.senderIcon),
            contentDescription = "",
            modifier = Modifier
                .size(36.dp, 36.dp)
                .clip(MaterialTheme.shapes.medium),
        )
        Surface(modifier = Modifier
            .offset(x = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    onLongPress(message)
                })
            }
            .wrapContentSize(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
            elevation = 1.dp) {
            Text(
                text = message.summary, modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ChatMessageSend(message: HomeMessage, onLongPress: (HomeMessage) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 56.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
    ) {
        val (messageText, spacer, icon) = createRefs()
        Image(
            painter = rememberImagePainter(data = message.senderIcon),
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
        Surface(modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    onLongPress(message)
                })
            }
            .wrapContentWidth(Alignment.End)
            .constrainAs(messageText) {
                top.linkTo(parent.top)
                end.linkTo(spacer.start)
            }, shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
            elevation = 1.dp
        ) {
            Text(
                text = message.summary, modifier = Modifier.padding(8.dp)
            )
        }
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
            0,
            "Hello ni hao",
            "",
            0,
            "FriendA",
            0,
            "Me",
            System.currentTimeMillis()
        )
    ) {}
}

@Composable
fun SendMessageInput(modifier: Modifier, onSendClick: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .background(Color(0xFFF0F0F0))
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .height(40.dp)
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                    IconButton(
                        onClick = {
                            onSendClick(text)
                            text = ""
                        },
                        enabled = text.isEmpty().not()
                    ) {
                        Icon(Icons.Filled.Send, null)
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageSendPreview() {
    ChatMessageSend(
        HomeMessage(
            0,
            "",
            "Hello",
            0,
            "Hello ni hao",
            "",
            0,
            "FriendA",
            0,
            "Me",
            System.currentTimeMillis()
        )
    ) {}
}

@Preview(showBackground = true)
@Composable
fun SendMessageInputPreview() {
    SendMessageInput(modifier = Modifier.fillMaxWidth()) {}
}

@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    ChatList(
        mutableListOf(
            HomeMessage(
                0,
                "",
                "Hello",
                0,
                "Hello ni hao",
                "",
                0,
                "FriendA",
                0,
                "Me",
                System.currentTimeMillis(),
            )
        ), {}, {}
    )
}