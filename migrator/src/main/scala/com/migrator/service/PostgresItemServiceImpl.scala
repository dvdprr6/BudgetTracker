package com.migrator.service

import com.migrator.models.{Item, PostgresConnectionDto}
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresItemRepository

trait PostgresItemService{
  def insertItemRecords(item: Seq[Item])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresItemServiceImpl(postgresItemRepository: PostgresItemRepository) extends PostgresItemService{

  override def insertItemRecords(item: Seq[Item])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] =
    for{
      _ <- postgresItemRepository.truncate()
      _ <- postgresItemRepository.insert(item)
    } yield ()
}

object PostgresItemServiceImpl{
  private def apply(postgresItemRepository: PostgresItemRepository): PostgresItemService =
    new PostgresItemServiceImpl(postgresItemRepository)

  lazy val live: ZLayer[PostgresItemRepository, Throwable, PostgresItemService] =
    ZLayer.fromFunction(apply _)
}
