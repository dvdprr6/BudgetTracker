package com.api.service

import com.api.models.{ItemDto, ItemEntity}
import com.api.repository.{ItemRepository}
import com.api.utils.Utils
import zio.{Task, ZLayer}

trait ItemService{
  def getItemRecords(): Task[Seq[ItemDto]]
  def getItemsByCategoryId(categoryId: String): Task[Seq[ItemDto]]
}

class ItemServiceImpl(itemRepository: ItemRepository) extends ItemService {

  override def getItemRecords(): Task[Seq[ItemDto]] = {
    val itemDtoRecords =
      for{
        itemByCategoryEntity <- itemRepository.getItems()
        itemByCategoryDto = itemByCategoryEntity.map(record => toItemDto(record))
      } yield itemByCategoryDto

    itemDtoRecords
  }

  override def getItemsByCategoryId(categoryId: String): Task[Seq[ItemDto]] = {
    val itemDtoRecords =
      for{
        itemByCategoryEntity <- itemRepository.getItemsByCategoryId(categoryId)
        itemByCategoryDto = itemByCategoryEntity.map(record => toItemDto(record))
      } yield itemByCategoryDto

    itemDtoRecords
  }

  private def toItemDto(itemEntity: ItemEntity): ItemDto = {
    val id = itemEntity.id
    val itemName = itemEntity.itemName
    val amount = itemEntity.amount
    val itemType = itemEntity.itemType
    val categoryId = itemEntity.categoryId
    val createDate = Utils.localDateTimeToString(itemEntity.createDate)
    val modifiedDate = Utils.localDateTimeToString(itemEntity.modifiedDate)

    ItemDto(id, itemName, amount, itemType, categoryId, createDate, modifiedDate)
  }
}

object ItemServiceImpl{
  private def apply(itemRepository: ItemRepository) =
    new ItemServiceImpl(itemRepository)

  lazy val live: ZLayer[ItemRepository, Throwable, ItemService] =
    ZLayer.fromFunction(apply _)
}
