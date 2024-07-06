package com.migrator.service

import com.migrator.models.Item
import com.migrator.repository.MongoDbItemRepository
import zio.{Task, ZLayer}

trait MongoDbItemService{
  def getItemRecords(mongoDbUrl: String): Task[Seq[Item]]
}

class MongoDbItemServiceImpl(mongoDbItemRepository: MongoDbItemRepository) extends MongoDbItemService {

  override def getItemRecords(mongoDbUrl: String): Task[Seq[Item]] = {
    for{
      item <- mongoDbItemRepository.getItemRecords(mongoDbUrl)
    } yield item
  }
}

object MongoDbItemServiceImpl{

  private def apply(mongoDbItemRepository: MongoDbItemRepository): MongoDbItemService =
    new MongoDbItemServiceImpl(mongoDbItemRepository)

  lazy val live: ZLayer[MongoDbItemRepository, Throwable, MongoDbItemService] =
    ZLayer.fromFunction(apply _)
}
