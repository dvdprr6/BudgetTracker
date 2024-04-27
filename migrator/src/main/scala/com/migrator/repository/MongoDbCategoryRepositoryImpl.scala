package com.migrator.repository

import com.commons.models.Category
import com.migrator.utils.Constants.MONGODB_DATABASE
import com.mongodb.client.{MongoClient, MongoCollection, MongoDatabase}
import zio.{Task, ZIO, ZLayer}

trait MongoDbCategoryRepository{
  def getCategoryRecords(mongoClient: MongoClient): Task[Seq[Category]]
}

class MongoDbCategoryRepositoryImpl extends MongoDbCategoryRepository {

  override def getCategoryRecords(mongoClient: MongoClient): Task[Seq[Category]] = ZIO.succeed{
    val database: MongoDatabase = mongoClient.getDatabase(MONGODB_DATABASE)
    val collection: MongoCollection[Category] = database.getCollection("categories", classOf[Category])

    var categoryDocuments: Seq[Category] = Seq()

    collection.find().forEach{record =>
      categoryDocuments = categoryDocuments :+ record
    }

    categoryDocuments
  }
}

object MongoDbCategoryRepositoryImpl{
  private def apply: MongoDbCategoryRepository = new MongoDbCategoryRepositoryImpl

  lazy val live: ZLayer[Any, Nothing, MongoDbCategoryRepository] =
    ZLayer.succeed(apply)
}
