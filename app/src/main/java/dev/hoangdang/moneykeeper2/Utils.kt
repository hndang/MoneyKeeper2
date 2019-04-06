package dev.hoangdang.moneykeeper2

import java.text.SimpleDateFormat
import java.util.*

const val datePatternDB = "yyyyMMdd"
const val datePatternView = "dd.MMMM.yyyy"
const val timePatternDB = "HHmmss"
const val timePatternView = "HH:mm:ss"

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
