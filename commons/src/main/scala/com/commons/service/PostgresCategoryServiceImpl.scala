package com.commons.service

import com.commons.models.{Category, CategoryDto}
import com.commons.repository.PostgresCategoryRepository
import com.commons.utils.Utils
import zio.{Task, ZLayer}

trait PostgresCategoryService{
  def getCategoryRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[CategoryDto]]

  def insertCategoryRecords(category: Seq[Category])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresCategoryServiceImpl(postgresCategoryRepository: PostgresCategoryRepository) extends PostgresCategoryService{

  override def getCategoryRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[CategoryDto]] =
    for{
      categoryRecords <- postgresCategoryRepository.get()(postgresUrl, postgresUsername, postgresPassword)
      categoryDtoRecords = categoryRecords.map(record => Utils.categoryToCategoryDto(record))
    } yield categoryDtoRecords

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