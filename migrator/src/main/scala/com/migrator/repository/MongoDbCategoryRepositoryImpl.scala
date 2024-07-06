package com.migrator.repository

import com.migrator.models.Category
import com.migrator.utils.MongoDbConnection
import zio._

trait MongoDbCategoryRepository{
  def getCategoryRecords(mongoUrl: String): Task[Seq[Category]]
}

class MongoDbCategoryRepositoryImpl(mongoDbConnection: MongoDbConnection) extends MongoDbCategoryRepository {
  override def getCategoryRecords(mongoUrl: String): Task[Seq[Category]] =
    for{
      categoryRecords <- mongoDbConnection.getMongoRecords[Category](mongoUrl, "categories", classOf[Category])
    } yield categoryRecords
}

object MongoDbCategoryRepositoryImpl{
  private def apply(mongoDbConnection: MongoDbConnection): MongoDbCategoryRepository =
    new MongoDbCategoryRepositoryImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Nothing, MongoDbCategoryRepository] =
    ZLayer.fromFunction(apply _)
}
