package com.migrator.repository

import com.migrator.models.Item
import com.migrator.utils.MongoDbConnection
import zio._

trait MongoDbItemRepository{
  def getItemRecords(): Task[Seq[Item]]
}

class MongoDbItemRepositoryImpl(mongoDbConnection: MongoDbConnection) extends MongoDbItemRepository {
  private val COLLECTION_ITEMS = "items"

  override def getItemRecords(): Task[Seq[Item]] =
    for{
      itemRecords <- mongoDbConnection.getMongoRecords[Item](COLLECTION_ITEMS, classOf[Item])
    } yield itemRecords
}

object MongoDbItemRepositoryImpl{
  private def apply(mongoDbConnection: MongoDbConnection): MongoDbItemRepository =
    new MongoDbItemRepositoryImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Nothing, MongoDbItemRepository] =
    ZLayer.fromFunction(apply _)
}
