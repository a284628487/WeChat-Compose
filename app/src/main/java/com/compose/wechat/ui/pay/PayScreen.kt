package com.compose.wechat.ui.pay

import android.content.Context
import android.graphics.PointF
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.wechat.ui.common.CommonTopBar
import com.compose.wechat.ui.theme.Red200
import com.compose.wechat.ui.theme.Red700
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

const val PWD_STATE_NOT_EXISTED = 0
const val PWD_STATE_SET_REPEAT = 1
const val PWD_STATE_SET_REPEAT_OK = 2
const val PWD_STATE_EXISTED = 3

const val PWD_KEY = "pay_pwd"

@Composable
fun PayContentScreen(
    navController: NavHostController,
    statusBarColor: MutableState<Color>
) {
    Scaffold(topBar = {
        statusBarColor.value = MaterialTheme.colors.primary
        CommonTopBar(navController, "我的账户")
    }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Row() {
                Icon(imageVector = Icons.Filled.AttachMoney, contentDescription = "")
                Text(text = "10,000,000", style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Composable
fun PayPasswordInputScreen(
    navController: NavHostController,
    statusBarColor: MutableState<Color>,
    setPwdComplete: () -> Unit = {},
    inputPwdComplete: () -> Unit = {}
) {
    Scaffold(topBar = {
        statusBarColor.value = MaterialTheme.colors.primary
        CommonTopBar(navController, "安全支付")
    }) {
        PayPasswordInput(
            scope = rememberCoroutineScope(),
            setPwdComplete,
            inputPwdComplete
        )
    }
}

@Composable
fun PayPasswordInput(
    scope: CoroutineScope,
    setPwdComplete: () -> Unit = {},
    inputPwdComplete: () -> Unit = {}
) {
    val indexes = remember {
        mutableStateListOf<Int>()
    }
    val context = LocalContext.current
    val sp = context.getSharedPreferences("wechat", Context.MODE_PRIVATE)
    var passWord = sp.getString(PWD_KEY, null)
    val tempPwd = remember {
        mutableStateOf("")
    }
    val setPwdState = remember {
        val initialState = if (passWord.isNullOrEmpty()) {
            PWD_STATE_NOT_EXISTED
        } else {
            PWD_STATE_EXISTED
        }
        mutableStateOf(initialState)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (passWord.isNullOrEmpty()) {
                when (setPwdState.value) {
                    PWD_STATE_SET_REPEAT -> {
                        Text(text = "请重复手势密码")
                    }
                    PWD_STATE_NOT_EXISTED -> {
                        Text(text = "请设置手势密码")
                    }
                    else -> {
                        // set password done
                        sp.edit().putString(PWD_KEY, tempPwd.value).apply()
                        setPwdComplete()
                    }
                }
            } else {
                Text(text = "请输入手势密码")
            }
        }
        PasswordInputWidget(modifier = Modifier.size(300.dp), indexes) {
            if (setPwdState.value == PWD_STATE_EXISTED) {
                if (passWord == indexes.joinToString(",")) {
                    inputPwdComplete()
                } else {
                    Toast.makeText(context, "密码输入错误,请重新输入", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (setPwdState.value == PWD_STATE_NOT_EXISTED) {
                    tempPwd.value = indexes.joinToString(",")
                    setPwdState.value = PWD_STATE_SET_REPEAT
                } else if (setPwdState.value == PWD_STATE_SET_REPEAT) {
                    val temp = indexes.joinToString(",")
                    if (temp == tempPwd.value) {
                        setPwdState.value = PWD_STATE_SET_REPEAT_OK
                    } else {
                        setPwdState.value = PWD_STATE_NOT_EXISTED
                        Toast.makeText(context, "密码和前次密码不同,请重新开始设置", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            scope.launch {
                delay(200)
                indexes.clear()
            }
        }
        if (setPwdState.value == PWD_STATE_EXISTED) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                TextButton(
                    onClick = {
                        sp.edit().putString(PWD_KEY, "").apply()
                        setPwdState.value = PWD_STATE_NOT_EXISTED
                        passWord = ""
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.error)
                ) {
                    Text(text = "重置密码")
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordInputWidget(
    modifier: Modifier,
    indexes: SnapshotStateList<Int> = remember {
        mutableStateListOf()
    },
    touchState: MutableState<Int> = remember {
        mutableStateOf(0)
    },
    onEnd: () -> Unit = {}
) {
    val radius = 90F
    val defaultColor = Color.LightGray
    val checkedColor = Red200
    val primaryColor = Red700

    val touchPoint = remember {
        mutableStateOf(PointF(0F, 0F))
    }

    Canvas(modifier = modifier.pointerInteropFilter {
        if ((it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_CANCEL)
            || indexes.size == 9
        ) {
            touchPoint.value = PointF(0F, 0F)
            onEnd()
            touchState.value = 0
            return@pointerInteropFilter false
        }
        if (it.action == MotionEvent.ACTION_DOWN) {
            touchState.value = 1
        }
        if (touchState.value == 1) {
            touchPoint.value = PointF(it.x, it.y)
        }
        true
    }, onDraw = {
        val xPadding = 48
        val yPadding = 48

        val hSpace = (size.width - xPadding * 2 - radius * 6) / 2
        val vSpace = (size.height - yPadding * 2 - radius * 6) / 2
        val centers = mutableListOf<Offset>()

        for (i in 0 until 9) {
            val xIndex = i % 3
            val yIndex = i / 3
            var xOffset = xPadding + radius
            var yOffset = yPadding + radius
            if (xIndex > 0) {
                xOffset += xIndex * (radius * 2 + hSpace)
            }
            if (yIndex > 0) {
                yOffset += yIndex * (radius * 2 + vSpace)
            }
            val color = if (indexes.contains(i)) {
                checkedColor
            } else {
                defaultColor
            }
            centers.add(Offset(xOffset, yOffset))
            drawCircle(
                color = color,
                radius = radius,
                center = centers[i]
            )
            //
            if (abs(centers[i].x - touchPoint.value.x) < radius * 2 / 3 &&
                abs(centers[i].y - touchPoint.value.y) < radius * 2 / 3
            ) {
                if (!indexes.contains(i)) {
                    indexes.add(i)
                }
            }
        }
        if (indexes.isNotEmpty()) {
            drawCircle(color = primaryColor, radius = 30F, center = centers[indexes[0]])

            val path = Path()
            path.moveTo(centers[indexes[0]].x, centers[indexes[0]].y)
            for (i in 1 until indexes.size) {
                path.lineTo(centers[indexes[i]].x, centers[indexes[i]].y)
            }
            with(touchPoint.value) {
                if (!(x == 0F && y == 0F)) {
                    path.lineTo(x, y)
                }
            }
            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 18F, cap = StrokeCap.Round)
            )

            for (i in 1 until indexes.size) {
                drawCircle(color = primaryColor, radius = 30F, center = centers[indexes[i]])
            }
        }
    })
}

@Composable
@Preview
fun PasswordInputWidgetPreview() {
    PayPasswordInput(rememberCoroutineScope())
}

