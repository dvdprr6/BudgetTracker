package com.api.repository

import com.api.models.CashFlowEntity
import com.api.utils.PostgresDbConnection
import zio._

trait CashFlowRepository{
  def getCashFlow(): Task[Seq[CashFlowEntity]]
}

class CashFlowRepositoryImpl(postgresDbConnection: PostgresDbConnection) extends CashFlowRepository{

  override def getCashFlow(): Task[Seq[CashFlowEntity]] = {
    val query =
      """
        |SELECT id, amount, delta, create_date, modified_date
        |FROM cash_flow""".stripMargin

    for{
      cashFlowRecords <- postgresDbConnection.get[CashFlowEntity](query, CashFlowEntity)
    } yield cashFlowRecords
  }
}

object CashFlowRepositoryImpl{
  private def apply(postgresDbConnection: PostgresDbConnection) =
    new CashFlowRepositoryImpl(postgresDbConnection)

  lazy val live: ZLayer[PostgresDbConnection, Throwable, CashFlowRepository] =
    ZLayer.fromFunction(apply _)
}
