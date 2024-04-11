package com.migrator.repository

import com.migrator.models.CashFlow
import com.migrator.utils.PostgresConnection
import zio.{ZIO, ZLayer}
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

trait PostgresCashFlowRepository{
  def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
}

class PostgresCashFlowRepositoryImpl extends PostgresCashFlowRepository with PostgresJdbcModule{

  import Entity._

  override def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int] = {
    val statement = insertInto(cashFlowTable)(objectId, amount, delta, createDate, modifiedDate).values(cashFlow)

    execute(statement).provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

  private object Entity{
    val cashFlowTable = defineTable[CashFlow]("cash_flow")

    val (objectId, amount, delta, createDate, modifiedDate) = cashFlowTable.columns
  }
}

object PostgresCashFlowRepositoryImpl{
  private def create(): PostgresCashFlowRepository = new PostgresCashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCashFlowRepository] =
    ZLayer.succeed(create)
}
