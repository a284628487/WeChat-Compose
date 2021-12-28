package com.compose.wechat.main.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VerifiedUser
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
import com.compose.wechat.entity.User
import com.compose.wechat.main.moments.ui.MomentsList


@Composable
fun ProfileScreen(
    user: User,
    list: List<JumpGroup>,
    onMenuClicked: (JumpConfig) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
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
            .background(Color.White)
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
                painter = painterResource(id = R.drawable.ic_frag),
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
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Icon(
                        imageVector = Icons.Filled.QrCode,
                        contentDescription = "",
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = "",
                        tint = Color.LightGray
                    )
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
                border = BorderStroke(width = 0.2.dp, color = Color.LightGray),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                Text(text = "+ 状态", style = MaterialTheme.typography.caption)
            }
            TextButton(
                onClick = onFriendStatusClicked,
                modifier = Modifier
                    .height(24.dp)
                    .padding(start = 12.dp),
                border = BorderStroke(width = 0.2.dp, color = Color.LightGray),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 1.dp, horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.VerifiedUser,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Usb,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
                Text(text = "2个朋友", style = MaterialTheme.typography.caption)
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
