package com.migrator.service

import com.migrator.models.{Category, PostgresConnectionDto}
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresCategoryRepository

trait PostgresCategoryService{
  def insertCategoryRecords(category: Seq[Category])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresCategoryServiceImpl(postgresCategoryRepository: PostgresCategoryRepository) extends PostgresCategoryService{

  override def insertCategoryRecords(category: Seq[Category])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] =
    for{
      _ <- postgresCategoryRepository.truncate()
      _ <- postgresCategoryRepository.insert(category)
    } yield ()
}

object PostgresCategoryServiceImpl{
  private def apply(postgresCategoryRepository: PostgresCategoryRepository): PostgresCategoryService =
    new PostgresCategoryServiceImpl(postgresCategoryRepository)

  lazy val live: ZLayer[PostgresCategoryRepository, Throwable, PostgresCategoryService] =
    ZLayer.fromFunction(apply _)
}