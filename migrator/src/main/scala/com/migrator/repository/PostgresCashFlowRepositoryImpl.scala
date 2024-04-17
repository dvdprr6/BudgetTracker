package com.migrator.repository

import com.migrator.models.CashFlow
import com.migrator.utils.{PostgresConnection, Utils}
import zio.schema.DeriveSchema
import zio.{ZIO, ZLayer}
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDateTime

trait PostgresCashFlowRepository{
  def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
}

class PostgresCashFlowRepositoryImpl extends PostgresCashFlowRepository with PostgresJdbcModule{

  import Entity._

  override def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int] = {

    val cashFlowEntity = cashFlow.map(record => toCashFlowEntity(record))

    val statement = insertInto(cashFlowTable)(id, amount, delta, createDate, modifiedDate).values(cashFlowEntity)

    execute(statement).provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

  private object Entity{
    case class CashFlowEntity(id: String,
                              amount: Double,
                              delta: Double,
                              createDate: LocalDateTime,
                              modifiedDate: LocalDateTime)


    implicit val customerSchema = DeriveSchema.gen[CashFlowEntity]

    val cashFlowTable = defineTable[CashFlowEntity]("cash_flow")

    val (id, amount, delta, createDate, modifiedDate) = cashFlowTable.columns

    def toCashFlowEntity(cashFlow: CashFlow): CashFlowEntity = {
      val createDateLocalDateTime: LocalDateTime = Utils.dateToLocalDateTime(cashFlow.createDate)
      val modifiedDateLocalDateTime: LocalDateTime = Utils.dateToLocalDateTime(cashFlow.modifiedDate)

      CashFlowEntity(cashFlow.id.toHexString, cashFlow.amount, cashFlow.delta, createDateLocalDateTime, modifiedDateLocalDateTime)
    }
  }
}

object PostgresCashFlowRepositoryImpl{
  private def apply: PostgresCashFlowRepository = new PostgresCashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCashFlowRepository] =
    ZLayer.succeed(apply)
}
