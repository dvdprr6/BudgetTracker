package com.migrator.service

import com.commons.models.CashFlow
import com.migrator.repository.MongoDbCashFlowRepository
import com.migrator.utils.MongoDbConnection
import zio.{Task, ZLayer}

trait MongoDbCashFlowService{
  def getCashFlowRecords(mongoDbUrl: String): Task[Seq[CashFlow]]
}

class MongoDbCashFlowServiceImpl(mongoDbConnection: MongoDbConnection, mongoDbCashFlowRepository: MongoDbCashFlowRepository) extends MongoDbCashFlowService {

  override def getCashFlowRecords(mongoDbUrl: String): Task[Seq[CashFlow]] =
    for {
      mongoDbClient <- mongoDbConnection.getMongoClient(mongoDbUrl)
      cashFlow <- mongoDbCashFlowRepository.getCashFlowRecords(mongoDbClient)
      _ = mongoDbClient.close()
    } yield cashFlow
}

object MongoDbCashFlowServiceImpl{
  private type MongoDbCashFlow = MongoDbConnection with MongoDbCashFlowRepository

  private def apply(mongoDbConnection: MongoDbConnection, mongoDbCashFlowRepository: MongoDbCashFlowRepository): MongoDbCashFlowService =
    new MongoDbCashFlowServiceImpl(mongoDbConnection, mongoDbCashFlowRepository)

  lazy val live: ZLayer[MongoDbCashFlow, Throwable, MongoDbCashFlowService] =
    ZLayer.fromFunction(apply _)
}
