package com.migrator.service

import com.migrator.models.{Category, PostgresConnectionDto}
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresCategoryRepository
import com.migrator.utils.PostgresDbConnection

trait PostgresCategoryService{
  def insertCategoryRecords(category: Seq[Category])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresCategoryServiceImpl(postgresCategoryRepository: PostgresCategoryRepository, postgresDbConnection: PostgresDbConnection) extends PostgresCategoryService{

  override def insertCategoryRecords(category: Seq[Category])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] =
    for{
      postgresSession <- postgresDbConnection.getPostgresSession
      _ <- postgresCategoryRepository.truncate()(postgresSession)
      _ <- postgresCategoryRepository.insert(category)(postgresSession)
    } yield ()
}

object PostgresCategoryServiceImpl{
  private type CategoryServiceItem = PostgresDbConnection with PostgresCategoryRepository

  private def apply(postgresCategoryRepository: PostgresCategoryRepository, postgresDbConnection: PostgresDbConnection): PostgresCategoryService =
    new PostgresCategoryServiceImpl(postgresCategoryRepository, postgresDbConnection)

  lazy val live: ZLayer[CategoryServiceItem, Throwable, PostgresCategoryService] =
    ZLayer.fromFunction(apply _)
}