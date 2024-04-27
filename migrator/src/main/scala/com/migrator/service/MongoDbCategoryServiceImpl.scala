package com.migrator.service

import com.commons.models.Category
import com.migrator.repository.MongoDbCategoryRepository
import com.migrator.utils.MongoDbConnection
import zio._

trait MongoDbCategoryService{
  def getCategoryRecords(mongoDbUrl: String): Task[Seq[Category]]
}

class MongoDbCategoryServiceImpl(mongoDbConnection: MongoDbConnection, mongoDbCategoryRepository: MongoDbCategoryRepository) extends MongoDbCategoryService{

  override def getCategoryRecords(mongoDbUrl: String): Task[Seq[Category]] =
    for{
      mongoDbClient <- mongoDbConnection.getMongoClient(mongoDbUrl)
      category <- mongoDbCategoryRepository.getCategoryRecords(mongoDbClient)
      _ = mongoDbClient.close()
    } yield category
}

object MongoDbCategoryServiceImpl{
  private type MongoDbCategory = MongoDbConnection with MongoDbCategoryRepository

  private def apply(mongoDbConnection: MongoDbConnection, mongoDbCategoryRepository: MongoDbCategoryRepository): MongoDbCategoryService =
    new MongoDbCategoryServiceImpl(mongoDbConnection, mongoDbCategoryRepository)

  lazy val live: ZLayer[MongoDbCategory, Throwable, MongoDbCategoryService] =
    ZLayer.fromFunction(apply _)
}