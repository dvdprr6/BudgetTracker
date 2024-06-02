package com.migrator.repository

import com.migrator.models.CashFlow
import com.migrator.utils.{PostgresConnection, Utils}
import scalikejdbc._
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresCashFlowRepository{
  def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresCashFlowRepositoryImpl extends PostgresCashFlowRepository with PostgresConnection {

  override def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] = ZIO.succeed {

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
}

object PostgresCashFlowRepositoryImpl{
  private def apply: PostgresCashFlowRepository = new PostgresCashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCashFlowRepository] =
    ZLayer.succeed(apply)
}
