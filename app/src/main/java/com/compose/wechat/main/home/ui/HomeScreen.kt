package com.compose.wechat.main.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.wechat.R
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.ui.theme.WeChatTheme

@Composable
fun HomeMessageList(messageList: List<HomeMessage>) {
    LazyColumn() {
        messageList.forEachIndexed { index, homeMessage ->
            item(key = index) {
                HomeMessageItem(message = homeMessage)
            }
        }
    }
}

@Composable
fun HomeMessageItem(message: HomeMessage) {
    val backgroundColor = if (message.isTopped) {
        Color(0xFFEBEBEB)
    } else {
        Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor)
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_frag),
            contentDescription = "",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                Text(
                    text = message.title,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = message.getFormatDate(),
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End),
                    style = MaterialTheme.typography.caption
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            ) {
                Text(
                    text = message.summary,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.caption
                )
                if (message.muted) {
                    Text(
                        text = "x",
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .offset(y = 9.5.dp),
                color = Color.LightGray,
                thickness = 0.3.dp
            )
        }
    }
}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = R.string.empty_content))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeMessageItemPreview() {
    WeChatTheme {
        HomeMessageItem(HomeMessage(0, "", "Title", "Summary", System.currentTimeMillis(), 0, ""))
    }
}