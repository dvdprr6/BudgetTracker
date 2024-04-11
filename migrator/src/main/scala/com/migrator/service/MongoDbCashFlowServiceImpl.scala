package com.migrator.service

import com.migrator.models.CashFlow
import com.migrator.repository.{MongoDbCashFlowRepository, MongoDbCashFlowRepositoryImpl}
import com.migrator.utils.MongoDbConnection
import zio.{Task, ZIO, ZLayer}

trait MongoDbCashFlowService{
  def getCashFlowRecords(mongoDbUrl: String): Task[Seq[CashFlow]]
}


class MongoDbCashFlowServiceImpl(mongoDbConnection: MongoDbConnection) extends MongoDbCashFlowService {

  override def getCashFlowRecords(mongoDbUrl: String): Task[Seq[CashFlow]] = {
    val program = for{
      cashFlowRepository <- ZIO.service[MongoDbCashFlowRepository]
      mongoDbClient <- mongoDbConnection.getMongoClient(mongoDbUrl)
      cashFlow <- cashFlowRepository.getCashFlowRecords(mongoDbClient)
      _ = mongoDbClient.close()
    } yield cashFlow

    program.provide(MongoDbCashFlowRepositoryImpl.live)
  }
}

object MongoDbCashFlowServiceImpl{
  private def create(mongoDbConnection: MongoDbConnection) =
    new MongoDbCashFlowServiceImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Throwable, MongoDbCashFlowService] =
    ZLayer.fromFunction(create _)
}
