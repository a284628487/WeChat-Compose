package com.compose.wechat.ui.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.compose.wechat.ui.common.CommonDivider

@Composable
fun MainPopupMenuPanel(showState: MutableState<Boolean>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        val popupWidth = 150.dp
        val cornerSize = 8.dp
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset(x = -16, y = 18)
        ) {
            Box(
                Modifier
                    .width(popupWidth)
                    .wrapContentHeight()
                    .background(
                        Color.DarkGray,
                        RoundedCornerShape(cornerSize)
                    )
            ) {
                Column {
                    val context = LocalContext.current
                    val clickCallback = { s: String ->
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
                        showState.value = false
                    }
                    AddMenuItem(Icons.Filled.Message, "????????????", clickCallback)
                    AddMenuItemDivider()
                    AddMenuItem(Icons.Filled.AddComment, "????????????", clickCallback)
                    AddMenuItemDivider()
                    AddMenuItem(Icons.Filled.Scanner, "?????????", clickCallback)
                    AddMenuItemDivider()
                    AddMenuItem(Icons.Filled.Payment, "?????????", clickCallback)
                }
            }
        }
    }
}

@Composable
fun AddMenuItem(
    imageVector: ImageVector,
    text: String,
    onClick: (String) -> Unit = {}
) {
    TextButton(
        onClick = {
            onClick(text)
        },
        modifier = Modifier.height(48.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
    ) {
        Icon(imageVector = imageVector, contentDescription = text)
        Text(
            text = text, modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun AddMenuItemDivider() {
    CommonDivider(Modifier.padding(start = 48.dp))
}