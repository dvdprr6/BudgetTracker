package com.commons.service

import com.commons.models.Category
import com.commons.repository.PostgresCategoryRepository
import zio.{Task, ZLayer}

trait PostgresCategoryService{
  def insertCategoryRecords(category: Seq[Category])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresCategoryServiceImpl(postgresCategoryRepository: PostgresCategoryRepository) extends PostgresCategoryService{

  override def insertCategoryRecords(category: Seq[Category])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] =
    for{
      _ <- postgresCategoryRepository.insert(category)(postgresUrl: String, postgresUsername: String, postgresPassword: String)
    } yield ()
}

object PostgresCategoryServiceImpl{
  private def apply(postgresCategoryRepository: PostgresCategoryRepository): PostgresCategoryService =
    new PostgresCategoryServiceImpl(postgresCategoryRepository)

  lazy val live: ZLayer[PostgresCategoryRepository, Throwable, PostgresCategoryService] =
    ZLayer.fromFunction(apply _)
}