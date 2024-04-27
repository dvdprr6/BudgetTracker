package com.commons.utils

import java.time.{Instant, LocalDateTime, ZoneId}
import java.util.Date

object Utils {

  def dateToLocalDateTime(date: Date): LocalDateTime = {
    val defaultZoneId: ZoneId = ZoneId.systemDefault
    val instant: Instant = date.toInstant

    val localDateTime: LocalDateTime = instant.atZone(defaultZoneId).toLocalDateTime

    localDateTime
  }

}
