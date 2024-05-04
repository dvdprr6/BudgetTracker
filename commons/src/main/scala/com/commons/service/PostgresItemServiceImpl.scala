package com.commons.service

import com.commons.models.{Item, ItemDto}
import com.commons.repository.PostgresItemRepository
import com.commons.utils.Utils
import zio.{Task, ZLayer}

trait PostgresItemService{
  def getItemRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[ItemDto]]

  def insertItemRecords(item: Seq[Item])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresItemServiceImpl(postgresItemRepository: PostgresItemRepository) extends PostgresItemService{

  override def getItemRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[ItemDto]] =
    for {
      itemRecords <- postgresItemRepository.get()(postgresUrl: String, postgresUsername: String, postgresPassword: String)
      itemDtoRecords = itemRecords.map(record => Utils.itemToItemDto(record))
    } yield itemDtoRecords

  override def insertItemRecords(item: Seq[Item])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] =
    for{
      _ <- postgresItemRepository.insert(item)(postgresUrl: String, postgresUsername: String, postgresPassword: String)
    } yield ()
}

object PostgresItemServiceImpl{
  private def apply(postgresItemRepository: PostgresItemRepository): PostgresItemService =
    new PostgresItemServiceImpl(postgresItemRepository)

  lazy val live: ZLayer[PostgresItemRepository, Throwable, PostgresItemService] =
    ZLayer.fromFunction(apply _)
}
