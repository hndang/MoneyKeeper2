package dev.hoangdang.moneykeeper2

import java.text.SimpleDateFormat
import java.util.*

fun getDateString(rawDate:Long, pattern:String): String{
    val calendar = Calendar.getInstance()
    val day = rawDate % 100
    val month = (rawDate/100) % 100
    val year = rawDate/10000
    calendar.set(year.toInt(),month.toInt(),day.toInt())
    return SimpleDateFormat(pattern).format(calendar.time)
}

fun convertDatePattern(rawDate: String, oldPattern: String, newPattern: String): String{
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(oldPattern)
    calendar.time = sdf.parse(rawDate)
    return SimpleDateFormat(newPattern).format(calendar.time)
}

fun getDateFromString(rawDate: String, oldPattern: String, newPattern: String): Long{
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(oldPattern)
    calendar.time = sdf.parse(rawDate)
    return SimpleDateFormat(newPattern).format(calendar.time).toLong()
}