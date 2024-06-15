package com.migrator.service

import com.migrator.models.{Item, PostgresConnectionDto}
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresItemRepository
import com.migrator.utils.PostgresDbConnection

trait PostgresItemService{
  def insertItemRecords(item: Seq[Item])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresItemServiceImpl(postgresItemRepository: PostgresItemRepository, postgresDbConnection: PostgresDbConnection) extends PostgresItemService{

  override def insertItemRecords(item: Seq[Item])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] =
    for{
      postgresSession <- postgresDbConnection.getPostgresSession
      _ <- postgresItemRepository.truncate()(postgresSession)
      _ <- postgresItemRepository.insert(item)(postgresSession)
    } yield ()
}

object PostgresItemServiceImpl{
  private type ItemServiceItem = PostgresDbConnection with PostgresItemRepository

  private def apply(postgresItemRepository: PostgresItemRepository, postgresDbConnection: PostgresDbConnection): PostgresItemService =
    new PostgresItemServiceImpl(postgresItemRepository, postgresDbConnection)

  lazy val live: ZLayer[ItemServiceItem, Throwable, PostgresItemService] =
    ZLayer.fromFunction(apply _)
}
