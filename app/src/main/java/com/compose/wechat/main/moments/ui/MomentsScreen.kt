package com.compose.wechat.main.moments.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.BlurCircular
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.wechat.R
import com.compose.wechat.entity.JumpConfig
import com.compose.wechat.entity.JumpGroup
import com.compose.wechat.ui.theme.WeChatTheme


@Composable
fun MomentsScreen(configs: List<JumpGroup>) {
    MomentsList(
        modifier = Modifier
            .padding(bottom = 56.dp)
            .fillMaxSize(),
        list = configs
    ) {
    }
}

@Composable
fun MomentsList(
    modifier: Modifier = Modifier,
    list: List<JumpGroup>,
    dividerFirst: Boolean = false,
    onMenuClicked: (JumpConfig) -> Unit = {}
) {
    LazyColumn(modifier = modifier.background(Color(0xFFEDEDED))) {
        list.forEachIndexed { index, jumpGroup ->
            if (index != 0) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                if (dividerFirst) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            jumpGroup.configs.forEachIndexed { innerIndex, jumpConfig ->
                item(index * 31 + (innerIndex + 1) * 3) {
                    JumpMenuItem(jump = jumpConfig, onMenuClicked)
                }
                item(index * 31 + (innerIndex + 1) * 3 + 1) {
                    Divider(
                        modifier = Modifier
                            .height(0.2.dp)
                            .offset(x = 40.dp)
                            .background(Color.LightGray)
                    )
                }
            }
        }
    }
}

@Composable
fun JumpMenuItem(jump: JumpConfig, onMenuClicked: (JumpConfig) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                onMenuClicked(jump)
            }
            .requiredHeightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = jump.imageVector!!,
            contentDescription = "",
            modifier = Modifier.width(40.dp)
        )
        Text(text = jump.name, modifier = Modifier.padding(end = 16.dp), color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        jump.content?.forEach {
            if (it is String) {
                Text(
                    text = it,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = Color.LightGray,
                    style = MaterialTheme.typography.caption
                )
            } else if (it is Int) {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(MaterialTheme.shapes.small)
                        .size(32.dp)
                )
            }
        }
        Icon(
            imageVector = Icons.Filled.ArrowRight,
            contentDescription = "",
            modifier = Modifier.width(24.dp),
            tint = Color.LightGray
        )
    }
}

@Preview
@Composable
fun MomentsItemPreview() {
    WeChatTheme {
        JumpMenuItem(
            JumpConfig(
                Icons.Filled.BlurCircular, "朋友圈", "", mutableListOf(
                    "ABCD", R.drawable.ic_frag
                )
            )
        ) {}
    }
}

@Preview
@Composable
fun MomentsListPreview() {
    WeChatTheme {
        Surface(modifier = Modifier.background(Color.White)) {
            MomentsList(
                list = listOf<JumpGroup>(
                    JumpGroup(
                        listOf(
                            JumpConfig(
                                Icons.Filled.BlurCircular, "朋友圈", "", mutableListOf(
                                    "ABCD", R.drawable.ic_frag
                                )
                            )
                        )
                    ),
                    JumpGroup(
                        listOf(
                            JumpConfig(
                                Icons.Filled.BlurCircular, "朋友圈", "", mutableListOf(
                                    "ABCD", R.drawable.ic_frag
                                )
                            ),
                            JumpConfig(
                                Icons.Filled.BlurCircular, "朋友圈", "", mutableListOf(
                                    "ABCD", R.drawable.ic_frag
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}