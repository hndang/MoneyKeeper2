package dev.hoangdang.moneykeeper2

import java.text.SimpleDateFormat
import java.util.*

const val DATE_PATTERN_DB = "yyyyMMdd"
const val DATE_PATTERN_VIEW = "dd.MMMM.yyyy"
const val TIME_PATTERN_DB = "HHmmss"
const val TIME_PATTERN_VIEW = "HH:mm:ss"

fun convertDatePattern(rawDate: String, oldPattern: String, newPattern: String): String{
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(oldPattern)
    calendar.time = sdf.parse(rawDate)
    return SimpleDateFormat(newPattern).format(calendar.time)
}

fun getCalendarFromPattern(rawDate: String, pattern: String):Calendar{
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(pattern)
    calendar.time = sdf.parse(rawDate)
    return calendar
}
