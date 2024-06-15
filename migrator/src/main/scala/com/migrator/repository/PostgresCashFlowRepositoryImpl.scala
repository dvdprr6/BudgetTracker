package com.migrator.repository

import com.migrator.models.{CashFlow, PostgresConnectionDto}
import com.migrator.utils.Utils
import scalikejdbc._
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresCashFlowRepository{
  def insert(cashFlow: Seq[CashFlow])(dbSession: DBSession): Task[Unit]
  def truncate()(dbSession: DBSession): Task[Unit]
}

class PostgresCashFlowRepositoryImpl extends PostgresCashFlowRepository {

  override def insert(cashFlow: Seq[CashFlow])(dbSession: DBSession): Task[Unit] = ZIO.succeed {

    implicit val session = dbSession

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

  override def truncate()(dbSession: DBSession): Task[Unit] = ZIO.succeed{
    implicit val session = dbSession

    sql"""truncate table cash_flow""".update.apply()
  }
}

object PostgresCashFlowRepositoryImpl{
  private def apply: PostgresCashFlowRepository = new PostgresCashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCashFlowRepository] =
    ZLayer.succeed(apply)
}
