package com.commons.repository

import com.commons.models.CashFlow
import com.commons.utils.{PostgresConnection, Utils}
import org.bson.types.ObjectId
import zio.schema.DeriveSchema
import zio.{ZIO, ZLayer}
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDateTime

trait PostgresCashFlowRepository{
  def get()(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Seq[CashFlow]]

  def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
}

class PostgresCashFlowRepositoryImpl extends PostgresCashFlowRepository with PostgresJdbcModule{

  import Entity._

  override def get()(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Seq[CashFlow]] = {
    val statement = select(id, amount, delta, createDate, modifiedDate).from(cashFlowTable)

    val executeStatement = for{
      cashFlowRecords <- execute(statement).map(CashFlowEntity tupled _).runCollect
      records = cashFlowRecords.map(record => toCashFlow(record))
    } yield records

    executeStatement.provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

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

    def toCashFlow(cashFlowEntity: CashFlowEntity): CashFlow = {
      val createDate = Utils.localDateTimeToDate(cashFlowEntity.createDate)
      val modifiedDate = Utils.localDateTimeToDate(cashFlowEntity.modifiedDate)

      val objectId = new ObjectId(cashFlowEntity.id)

      CashFlow(objectId, cashFlowEntity.amount, cashFlowEntity.delta, createDate, modifiedDate)
    }
  }
}

object PostgresCashFlowRepositoryImpl{
  private def apply: PostgresCashFlowRepository = new PostgresCashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCashFlowRepository] =
    ZLayer.succeed(apply)
}
