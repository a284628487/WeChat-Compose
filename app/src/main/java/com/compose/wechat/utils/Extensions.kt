package com.compose.wechat.utils

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