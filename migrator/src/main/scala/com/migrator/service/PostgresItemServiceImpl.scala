package com.migrator.service

import com.migrator.models.Item
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresItemRepository

trait PostgresItemService{
  def insertItemRecords(item: Seq[Item]): Task[Unit]
}

class PostgresItemServiceImpl(postgresItemRepository: PostgresItemRepository) extends PostgresItemService{

  override def insertItemRecords(item: Seq[Item]): Task[Unit] =
    for{
      _ <- postgresItemRepository.truncateItem()
      _ <- postgresItemRepository.insertItem(item)
    } yield ()
}

object PostgresItemServiceImpl{
  private def apply(postgresItemRepository: PostgresItemRepository): PostgresItemService =
    new PostgresItemServiceImpl(postgresItemRepository)

  lazy val live: ZLayer[PostgresItemRepository, Throwable, PostgresItemService] =
    ZLayer.fromFunction(apply _)
}
