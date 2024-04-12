package com.migrator.service

import com.migrator.models.Item
import com.migrator.repository.{MongoDbItemRepository, MongoDbItemRepositoryImpl}
import com.migrator.utils.MongoDbConnection
import zio.{Task, ZIO, ZLayer}

trait MongoDbItemService{
  def getItemRecords(mongoDbUrl: String): Task[Seq[Item]]
}

class MongoDbItemServiceImpl(mongoDbConnection: MongoDbConnection, mongoDbItemRepository: MongoDbItemRepository) extends MongoDbItemService {

  override def getItemRecords(mongoDbUrl: String): Task[Seq[Item]] = {
    for{
      mongoDbClient <- mongoDbConnection.getMongoClient(mongoDbUrl)
      item <- mongoDbItemRepository.getItemRecords(mongoDbClient)
      _ = mongoDbClient.close()
    } yield item
  }
}

object MongoDbItemServiceImpl{
  private type MongoDbItem = MongoDbConnection with MongoDbItemRepository

  private def apply(mongoDbConnection: MongoDbConnection, mongoDbItemRepository: MongoDbItemRepository): MongoDbItemService =
    new MongoDbItemServiceImpl(mongoDbConnection, mongoDbItemRepository)

  lazy val live: ZLayer[MongoDbItem, Throwable, MongoDbItemService] =
    ZLayer.fromFunction(apply _)
}
