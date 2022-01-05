package com.compose.wechat.ui.main.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.wechat.R
import com.compose.wechat.entity.JumpConfig
import com.compose.wechat.entity.JumpGroup
import com.compose.wechat.entity.User
import com.compose.wechat.ui.common.ArrowIcon
import com.compose.wechat.ui.main.moments.MomentsList


@Composable
fun ProfileScreen(
    user: User,
    list: List<JumpGroup>,
    onMenuClicked: (JumpConfig) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        ProfileHeader(user = user, {}, {}, {})
        ProfileMenusList(list = list, onMenuClicked = onMenuClicked)
    }
}

@Composable
fun ProfileHeader(
    user: User,
    onHeadClicked: () -> Unit,
    onStatusClicked: () -> Unit,
    onFriendStatusClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .padding(bottom = 12.dp)
            .wrapContentSize()
    ) {
        Row(modifier = Modifier
            .clickable {
                onHeadClicked()
            }
            .padding(start = 12.dp, top = 24.dp)
            .fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.my_head),
                contentDescription = "",
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = user.name, style = MaterialTheme.typography.h6)
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "微信号: ${user.id}",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Icon(
                        imageVector = Icons.Filled.QrCode,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(18.dp)
                    )
                    ArrowIcon()
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 84.dp, top = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TextButton(
                onClick = onStatusClicked,
                modifier = Modifier.height(24.dp),
                border = BorderStroke(width = 0.2.dp, color = MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                Text(
                    text = "+ 状态",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onBackground
                )
            }
            TextButton(
                onClick = onFriendStatusClicked,
                modifier = Modifier
                    .height(24.dp)
                    .padding(start = 12.dp),
                border = BorderStroke(width = 0.2.dp, color = MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 1.dp, horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.VerifiedUser,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colors.onBackground
                )
                Icon(
                    imageVector = Icons.Filled.Usb,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "2个朋友",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Composable
fun ProfileMenusList(
    modifier: Modifier = Modifier,
    list: List<JumpGroup>,
    onMenuClicked: (JumpConfig) -> Unit = {}
) = MomentsList(
    modifier = modifier,
    list = list,
    dividerFirst = true,
    onMenuClicked = onMenuClicked
)

@Preview
@Composable
fun ProfilePreview() {
    ProfileHeader(User("cj758661", "Ccflying88"), {}, {}, {})
}
