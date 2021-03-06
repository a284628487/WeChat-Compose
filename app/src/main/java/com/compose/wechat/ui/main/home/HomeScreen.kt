package com.compose.wechat.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.compose.wechat.R
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.entity.UiState
import com.compose.wechat.ui.common.CommonDivider
import com.compose.wechat.ui.theme.WeChatTheme

@Composable
fun HomeScreen(uiState: UiState<List<HomeMessage>>, onMessageClick: (HomeMessage) -> Unit) {
    if (uiState.loading) {
        // TODO loading
    } else {
        if (uiState.data.isNullOrEmpty()) {
            EmptyView()
        } else {
            LazyColumn {
                uiState.data.forEachIndexed { index, homeMessage ->
                    item(key = index) {
                        HomeMessageItem(message = homeMessage, onMessageClick)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeMessageItem(message: HomeMessage, onMessageClick: (HomeMessage) -> Unit) {
    val backgroundColor = if (message.isTopped) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.surface
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onMessageClick(message)
            }
            .background(backgroundColor)
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = message.sessionIcon),
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
            CommonDivider(modifier = Modifier.offset(y = 9.5.dp))
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
        HomeMessageItem(
            HomeMessage(
                0,
                "",
                "Title",
                0,
                "Summary",
                "",
                0,
                "",
                0,
                "",
                System.currentTimeMillis()
            )
        ) {}
    }
}