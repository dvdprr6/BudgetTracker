package com.migrator.service

import com.migrator.models.Category
import com.migrator.repository.MongoDbCategoryRepository
import zio._

trait MongoDbCategoryService{
  def getCategoryRecords(): Task[Seq[Category]]
}

class MongoDbCategoryServiceImpl(mongoDbCategoryRepository: MongoDbCategoryRepository) extends MongoDbCategoryService{

  override def getCategoryRecords(): Task[Seq[Category]] =
    for{
      category <- mongoDbCategoryRepository.getCategoryRecords()
    } yield category
}

object MongoDbCategoryServiceImpl{

  private def apply(mongoDbCategoryRepository: MongoDbCategoryRepository): MongoDbCategoryService =
    new MongoDbCategoryServiceImpl(mongoDbCategoryRepository)

  lazy val live: ZLayer[MongoDbCategoryRepository, Throwable, MongoDbCategoryService] =
    ZLayer.fromFunction(apply _)
}