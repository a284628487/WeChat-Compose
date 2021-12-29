package com.compose.wechat.utils

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import java.text.SimpleDateFormat
import java.util.*

val dateFormatAll = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
val dateFormatHourMinute = SimpleDateFormat("HH:mm")
val dateFormatYearMonthDay = SimpleDateFormat("yyyy-MM-dd")
val dateFormatMonthDay = SimpleDateFormat("MM月dd日")
const val millsOfMin = 60 * 1000L
const val millsOfHour = millsOfMin * 60L
const val millsOfDay = millsOfHour * 24

fun Long.toDateString(): String {
    val today = dateFormatYearMonthDay.format(Date(System.currentTimeMillis()))
    val todayStamp = dateFormatYearMonthDay.parse(today).time
    val yesterdayStamp = todayStamp - millsOfDay
    if (this > todayStamp) {
        return dateFormatHourMinute.format(Date(this))
    } else if (this > yesterdayStamp) {
        return "昨天"
    }
    return dateFormatMonthDay.format(Date(this))
}

inline fun <reified T> logd(message: Any) {
    Log.d("${T::class.simpleName}", "$message")
}

inline fun <reified T> logi(message: Any) {
    Log.i("${T::class.simpleName}", "$message")
}

inline fun <reified T> loge(message: Any) {
    Log.e("${T::class.simpleName}", "$message")
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.touchSwitchState(mutableState: MutableState<Boolean>): Modifier =
    this.pointerInteropFilter {
        if (it.action == MotionEvent.ACTION_DOWN) {
            if (mutableState.value) {
                mutableState.value = false
                return@pointerInteropFilter true
            }
        }
        false
    }

fun Modifier.onPress(onPress: suspend PressGestureScope.(Offset) -> Unit): Modifier =
    this.pointerInput(Unit) {
        detectTapGestures(onPress = onPress)
    }