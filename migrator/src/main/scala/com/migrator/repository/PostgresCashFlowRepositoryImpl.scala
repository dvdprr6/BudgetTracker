package com.migrator.repository

import com.migrator.models.CashFlow
import com.migrator.utils.{PostgresDbConnection, Utils}
import scalikejdbc.{NoExtractor, SQL, scalikejdbcSQLInterpolationImplicitDef}
import zio._

trait PostgresCashFlowRepository{
  def insertCashFlow(cashFlow: Seq[CashFlow]): Task[Unit]
  def truncateCashFlow(): Task[Unit]
}

class PostgresCashFlowRepositoryImpl(postgresDbConnection: PostgresDbConnection) extends PostgresCashFlowRepository {

  override def insertCashFlow(cashFlow: Seq[CashFlow]): Task[Unit] = {
    val batchParams: Seq[Seq[(String, Any)]] = cashFlow.map { record =>
      Seq(
        "id" -> record.id.toHexString,
        "amount" -> record.amount,
        "delta" -> record.delta,
        "create_date" -> Utils.dateToLocalDateTime(record.createDate),
        "modified_date" -> Utils.dateToLocalDateTime(record.modifiedDate)
      )
    }

    val insertQuery =
      """
        |INSERT INTO cash_flow (id, amount, delta, create_date, modified_date)
        |VALUES ({id}, {amount}, {delta}, {create_date}, {modified_date})""".stripMargin

    for{
      _ <- postgresDbConnection.insert(insertQuery, batchParams)
    } yield()
  }

  override def truncateCashFlow(): Task[Unit] = {
    for{
      _ <- postgresDbConnection.truncate("cash_flow")
    } yield ()
  }
}

object PostgresCashFlowRepositoryImpl{
  private def apply(postgresDbConnection: PostgresDbConnection): PostgresCashFlowRepository =
    new PostgresCashFlowRepositoryImpl(postgresDbConnection)

  lazy val live: ZLayer[PostgresDbConnection, Throwable, PostgresCashFlowRepository] =
    ZLayer.fromFunction(apply _)
}
