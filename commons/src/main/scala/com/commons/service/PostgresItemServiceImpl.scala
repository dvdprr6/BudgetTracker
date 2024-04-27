package com.commons.service

import com.commons.models.Item
import com.commons.repository.PostgresItemRepository
import zio.{Task, ZLayer}

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
