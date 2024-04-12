package com.migrator.service

import com.migrator.models.Item
import com.migrator.repository.{MongoDbItemRepository, MongoDbItemRepositoryImpl}
import com.migrator.utils.MongoDbConnection
import zio.{Task, ZIO, ZLayer}

trait MongoDbItemService{
  def getItemRecords(mongoDbUrl: String): Task[Seq[Item]]
}

class MongoDbItemServiceImpl(mongoDbConnection: MongoDbConnection) extends MongoDbItemService {

  override def getItemRecords(mongoDbUrl: String): Task[Seq[Item]] = {
    val program = for{
      mongoDbItemRepository <- ZIO.service[MongoDbItemRepository]
      mongoDbClient <- mongoDbConnection.getMongoClient(mongoDbUrl)
      item <- mongoDbItemRepository.getItemRecords(mongoDbClient)
      _ = mongoDbClient.close()
    } yield item

    program.provide(MongoDbItemRepositoryImpl.live)
  }
}

object MongoDbItemServiceImpl{
  private def apply(mongoDbConnection: MongoDbConnection) =
    new MongoDbItemServiceImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Throwable, MongoDbItemService] =
    ZLayer.fromFunction(apply _)
}
