package com.migrator.service

import com.migrator.models.Item
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresItemRepository

trait PostgresItemService{
  def insertItemRecords(item: Seq[Item])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresItemServiceImpl(postgresItemRepository: PostgresItemRepository) extends PostgresItemService{

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
