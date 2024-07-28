package com.migrator.service

import com.migrator.models.CashFlow
import com.migrator.repository.MongoDbCashFlowRepository
import zio.{Task, ZLayer}

trait MongoDbCashFlowService{
  def getCashFlowRecords(): Task[Seq[CashFlow]]
}

class MongoDbCashFlowServiceImpl(mongoDbCashFlowRepository: MongoDbCashFlowRepository) extends MongoDbCashFlowService {

  override def getCashFlowRecords(): Task[Seq[CashFlow]] =
    for {
      cashFlow <- mongoDbCashFlowRepository.getCashFlowRecords()
    } yield cashFlow
}

object MongoDbCashFlowServiceImpl{

  private def apply(mongoDbCashFlowRepository: MongoDbCashFlowRepository): MongoDbCashFlowService =
    new MongoDbCashFlowServiceImpl(mongoDbCashFlowRepository)

  lazy val live: ZLayer[MongoDbCashFlowRepository, Throwable, MongoDbCashFlowService] =
    ZLayer.fromFunction(apply _)
}
