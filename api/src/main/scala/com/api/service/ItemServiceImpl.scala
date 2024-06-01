package com.api.service

import com.api.models.{ItemDto, ItemEntity, PostgresConnectionDto}
import com.api.repository.{ItemRepository, ItemRepositoryImpl}
import com.api.utils.Utils
import zio.{Task, ZIO, ZLayer}

trait ItemService{
  def getItemsByCategoryId(categoryId: String)(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[ItemDto]]
}

class ItemServiceImpl extends ItemService {

  override def getItemsByCategoryId(categoryId: String)(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[ItemDto]] = {
    val itemDtoRecords =
      for{
        itemRepository <- ZIO.service[ItemRepository]
        itemByCategoryEntity <- itemRepository.getItemsByCategoryId(categoryId)
        itemByCategoryDto = itemByCategoryEntity.map(record => toItemDto(record))
      } yield itemByCategoryDto

    itemDtoRecords.provide(ItemRepositoryImpl.live)

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
  private def apply = new ItemServiceImpl

  lazy val live: ZLayer[Any, Throwable, ItemService] =
    ZLayer.succeed(apply)
}
