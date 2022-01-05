package com.compose.wechat.ui.pay

import android.content.Context
import android.graphics.PointF
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.compose.wechat.ui.theme.Red200
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PayPasswordInputScreen(scope: CoroutineScope) {
    val indexes = remember {
        mutableStateListOf<Int>()
    }
    val context = LocalContext.current
    val sp = context.getSharedPreferences("wechat", Context.MODE_PRIVATE)
    val passWord = sp.getString("pay_pwd", null)
    var tempPwd = ""
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val setPwdState = remember {
            mutableStateOf(0)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (passWord.isNullOrEmpty()) {
                if (setPwdState.value == 1) {
                    Text(text = "请重复手势密码")
                } else if (setPwdState.value == 0) {
                    Text(text = "请设置手势密码")
                } else {
                    // done
                    sp.edit().putString("pay_pwd", indexes.joinToString(",")).apply()
                }
            } else {
                Text(text = "请输入手势密码")
            }
        }
        PasswordInputWidget(modifier = Modifier.size(300.dp), indexes) {
            scope.launch {
                if (setPwdState.value == 0) {
                    tempPwd = indexes.joinToString(",")
                    setPwdState.value = 1
                } else if (setPwdState.value == 1) {
                    val temp = indexes.joinToString(",")
                    if (temp == tempPwd) {
                        setPwdState.value = 2
                    } else {
                        setPwdState.value = 0
                    }
                }
                delay(200)
                indexes.clear()
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
    val primaryColor = MaterialTheme.colors.primary

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
    PayPasswordInputScreen(rememberCoroutineScope())
}

