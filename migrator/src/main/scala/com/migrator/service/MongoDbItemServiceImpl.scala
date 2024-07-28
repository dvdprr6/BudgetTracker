package com.migrator.service

import com.migrator.models.Item
import com.migrator.repository.MongoDbItemRepository
import zio.{Task, ZLayer}

trait MongoDbItemService{
  def getItemRecords(): Task[Seq[Item]]
}

class MongoDbItemServiceImpl(mongoDbItemRepository: MongoDbItemRepository) extends MongoDbItemService {

  override def getItemRecords(): Task[Seq[Item]] = {
    for{
      item <- mongoDbItemRepository.getItemRecords()
    } yield item
  }
}

object MongoDbItemServiceImpl{

  private def apply(mongoDbItemRepository: MongoDbItemRepository): MongoDbItemService =
    new MongoDbItemServiceImpl(mongoDbItemRepository)

  lazy val live: ZLayer[MongoDbItemRepository, Throwable, MongoDbItemService] =
    ZLayer.fromFunction(apply _)
}
