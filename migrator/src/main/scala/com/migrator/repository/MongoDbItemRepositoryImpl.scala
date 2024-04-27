package com.migrator.repository

import com.commons.models.Item
import com.migrator.utils.Constants.MONGODB_DATABASE
import com.mongodb.client.{MongoClient, MongoCollection, MongoDatabase}
import zio.{Task, ZIO, ZLayer}

trait MongoDbItemRepository{
  def getItemRecords(mongoClient: MongoClient): Task[Seq[Item]]
}

class MongoDbItemRepositoryImpl extends MongoDbItemRepository {

  override def getItemRecords(mongoClient: MongoClient): Task[Seq[Item]] = ZIO.succeed{
    val database: MongoDatabase = mongoClient.getDatabase(MONGODB_DATABASE)
    val collection: MongoCollection[Item] = database.getCollection("items", classOf[Item])

    var itemDocuments: Seq[Item] = Seq()

    collection.find.forEach{record =>
      itemDocuments = itemDocuments :+ record
    }

    itemDocuments
  }
}

object MongoDbItemRepositoryImpl{
  private def apply: MongoDbItemRepository = new MongoDbItemRepositoryImpl

  lazy val live: ZLayer[Any, Nothing, MongoDbItemRepository] =
    ZLayer.succeed(apply)
}
