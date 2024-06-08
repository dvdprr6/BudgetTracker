package com.migrator.repository

import com.migrator.models.{CashFlow, PostgresConnectionDto}
import com.migrator.utils.{PostgresConnection, Utils}
import scalikejdbc._
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresCashFlowRepository{
  def insert(cashFlow: Seq[CashFlow])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
  def truncate()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresCashFlowRepositoryImpl extends PostgresCashFlowRepository with PostgresConnection {

  override def insert(cashFlow: Seq[CashFlow])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] = ZIO.succeed {
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    val batchParams: Seq[Seq[(String, Any)]] = cashFlow.map { record =>
      Seq(
        "id" -> record.id.toHexString,
        "amount" -> record.amount,
        "delta" -> record.delta,
        "create_date" -> Utils.dateToLocalDateTime(record.createDate),
        "modified_date" -> Utils.dateToLocalDateTime(record.modifiedDate)
      )
    }

    sql"""
          insert into cash_flow (id, amount, delta, create_date, modified_date)
          values ({id}, {amount}, {delta}, {create_date}, {modified_date})
          """
      .batchByName(batchParams: _*)
      .apply
  }

  override def truncate()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] = ZIO.succeed{
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    sql"""truncate table cash_flow""".update.apply()
  }
}

object PostgresCashFlowRepositoryImpl{
  private def apply: PostgresCashFlowRepository = new PostgresCashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCashFlowRepository] =
    ZLayer.succeed(apply)
}
