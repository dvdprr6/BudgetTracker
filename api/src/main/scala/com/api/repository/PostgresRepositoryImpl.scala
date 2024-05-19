package com.api.repository

import com.api.models.{CashFlowDto, ItemDto}
import com.api.utils.{PostgresConnection, Utils}
import zio._
import zio.schema.DeriveSchema
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDateTime

trait PostgresRepository{
  def getItems()(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Seq[ItemDto]]

  def getCashFlow()(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Seq[CashFlowDto]]
}

class PostgresRepositoryImpl extends PostgresRepository with PostgresJdbcModule{

  import Entity._

  override def getItems()(postgresUrl: TableName, postgresUsername: TableName, postgresPassword: TableName): ZIO[Any, Exception, Seq[ItemDto]] = ???

  override def getCashFlow()(postgresUrl: TableName, postgresUsername: TableName, postgresPassword: TableName): ZIO[Any, Exception, Seq[CashFlowDto]] = {
    val statement = select(id, amount, delta, createDate, modifiedDate).from(cashFlowTable)

    val executeStatement = for{
      cashFlowEntityRecords <- execute(statement).map(CashFlowEntity tupled _).runCollect
      records = cashFlowEntityRecords.map(record => toCashFlowDto(record))
    } yield records

    executeStatement.provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

  private object Entity{
    private val CASH_FLOW_TABLE = "cash_flow"

    /* CASH FLOW ENTITY */

    case class CashFlowEntity(id: String, amount: Double, delta: Double, createDate: LocalDateTime, modifiedDate: LocalDateTime)

    implicit val cashFlowSchema = DeriveSchema.gen[CashFlowEntity]

    val cashFlowTable = defineTable[CashFlowEntity](CASH_FLOW_TABLE)

    val (id, amount, delta, createDate, modifiedDate) = cashFlowTable.columns

    def toCashFlowDto(cashFlowEntity: CashFlowEntity): CashFlowDto = {
      val id = cashFlowEntity.id
      val amount = cashFlowEntity.amount
      val delta = cashFlowEntity.delta
      val createDate = Utils.localDateTimeToString(cashFlowEntity.createDate)
      val modifiedDate = Utils.localDateTimeToString(cashFlowEntity.modifiedDate)

      CashFlowDto(id, amount, delta, createDate, modifiedDate)
    }
  }
}

object PostgresRepositoryImpl{
  private def apply: PostgresRepository = new PostgresRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresRepository] =
    ZLayer.succeed(apply)
}
