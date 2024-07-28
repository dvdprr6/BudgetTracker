package com.migrator.repository

import com.migrator.models.CashFlow
import com.migrator.utils.MongoDbConnection
import zio._

trait MongoDbCashFlowRepository{
  def getCashFlowRecords(): Task[Seq[CashFlow]]
}

class MongoDbCashFlowRepositoryImpl(mongoDbConnection: MongoDbConnection) extends MongoDbCashFlowRepository{
  private val COLLECTION_CASH_FLOW = "cash_flow"

  override def getCashFlowRecords(): Task[Seq[CashFlow]] =
    for{
      cashFlowRecords <- mongoDbConnection.getMongoRecords[CashFlow](COLLECTION_CASH_FLOW, classOf[CashFlow])
    } yield cashFlowRecords
}

object MongoDbCashFlowRepositoryImpl{
  private def apply(mongoDbConnection: MongoDbConnection): MongoDbCashFlowRepository =
    new MongoDbCashFlowRepositoryImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Nothing, MongoDbCashFlowRepository] =
    ZLayer.fromFunction(apply _)
}
