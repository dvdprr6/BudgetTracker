package com.commons.utils

import com.commons.models.{Category, CategoryDto}

import java.text.{DateFormat, SimpleDateFormat}
import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}
import java.util.Date

object Utils {

  def dateToLocalDateTime(date: Date): LocalDateTime = {
    val defaultZoneId: ZoneId = ZoneId.systemDefault
    val instant: Instant = date.toInstant

    val localDateTime: LocalDateTime = instant.atZone(defaultZoneId).toLocalDateTime

    localDateTime
  }

  def localDateTimeToDate(localDateTime: LocalDateTime): Date = {
    val localDateInstant = localDateTime.toInstant(ZoneOffset.UTC)

    val date = Date.from(localDateInstant)

    date
  }

  def dateToString(date: Date): String = {
    val pattern = "yyyy-MM-dd HH:mm:ss"

    val dateFormat: DateFormat = new SimpleDateFormat(pattern)

    val dateString = dateFormat.format(date)

    dateString
  }

  def categoryToCategoryDto(category: Category): CategoryDto = {
    val id = category.id.toHexString
    val categoryName = category.categoryName
    val createDate = dateToString(category.createDate)
    val modifiedDate = dateToString(category.modifiedDate)

    CategoryDto(id, categoryName, createDate, modifiedDate)
  }

}
