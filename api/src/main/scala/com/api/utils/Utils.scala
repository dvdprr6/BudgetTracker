package com.api.utils

import java.text.{DateFormat, SimpleDateFormat}
import java.time.{LocalDateTime, ZoneId}
import java.util.Date

object Utils {

  def localDateTimeToString(localDateTime: LocalDateTime): String = {
    val zonedDateTime = localDateTime.atZone(ZoneId.systemDefault())

    val date = Date.from(zonedDateTime.toInstant)

    val pattern = "yyyy-MM-dd HH:mm:ss"

    val dateFormat: DateFormat = new SimpleDateFormat(pattern)

    val dateString = dateFormat.format(date)

    dateString
  }
}
