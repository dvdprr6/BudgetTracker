package com.commons.utils

import com.commons.models.{CashFlow, CashFlowDto, Category, CategoryDto, Item, ItemDto}
import zio.schema.Patch.ZonedDateTime

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
    val zonedDateTime = localDateTime.atZone(ZoneId.systemDefault())

    val date = Date.from(zonedDateTime.toInstant())

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

  def cashFlowToCashFlowDto(cashFlow: CashFlow): CashFlowDto = {
    val id = cashFlow.id.toHexString
    val amount = cashFlow.amount
    val delta = cashFlow.delta
    val createDate = dateToString(cashFlow.createDate)
    val modifiedDate = dateToString(cashFlow.modifiedDate)

    CashFlowDto(id, amount, delta, createDate, modifiedDate)
  }

  def itemToItemDto(item: Item): ItemDto = {
    val id = item.id.toHexString
    val itemName = item.itemName
    val amount = item.amount
    val itemType = item.itemType
    val categoryId = item.categoryId.toHexString
    val createDate = dateToString(item.createDate)
    val modifiedDate = dateToString(item.modifiedDate)

    ItemDto(id, itemName, amount, itemType, categoryId, createDate, modifiedDate)
  }
}
