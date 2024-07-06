package com.migrator.repository

import com.migrator.models.CashFlow
import com.migrator.utils.MongoDbConnection
import zio._

trait MongoDbCashFlowRepository{
  def getCashFlowRecords(mongoUrl: String): Task[Seq[CashFlow]]
}

class MongoDbCashFlowRepositoryImpl(mongoDbConnection: MongoDbConnection) extends MongoDbCashFlowRepository{

  override def getCashFlowRecords(mongoUrl: String): Task[Seq[CashFlow]] =
    for{
      cashFlowRecords <- mongoDbConnection.getMongoRecords[CashFlow](mongoUrl, "cash_flow", classOf[CashFlow])
    } yield cashFlowRecords
}

object MongoDbCashFlowRepositoryImpl{
  private def apply(mongoDbConnection: MongoDbConnection): MongoDbCashFlowRepository =
    new MongoDbCashFlowRepositoryImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Nothing, MongoDbCashFlowRepository] =
    ZLayer.fromFunction(apply _)
}
