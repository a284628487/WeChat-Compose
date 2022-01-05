package com.compose.wechat.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun FunctionalityNotAvailableDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Function not available. \uD83D\uDE48",
                style = MaterialTheme.typography.body1
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.error)
            ) {
                Text(text = "OK")
            }
        }
    )
}

@Composable
fun CommonTopBar(
    navController: NavHostController,
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
    }
    IconButton(onClick = {
        navController.popBackStack()
    }) {
        Icon(
            Icons.Filled.ArrowBack,
            "back",
            tint = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun ArrowIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Filled.ArrowRight,
        contentDescription = "arrowRight",
        modifier = modifier,
        tint = Color.LightGray
    )
}

@Composable
fun CommonDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier
            .height(0.3.dp)
            .background(Color.LightGray)
            .fillMaxWidth()
    )
}
