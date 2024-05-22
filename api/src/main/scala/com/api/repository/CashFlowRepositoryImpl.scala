package com.api.repository

import com.api.models.{CashFlowEntity, PostgresConnectionDto}
import com.api.utils.PostgresConnection
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef
import zio._

trait CashFlowRepository{
  def get()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CashFlowEntity]]
}

class CashFlowRepositoryImpl extends CashFlowRepository with PostgresConnection {

  override def get()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CashFlowEntity]] = ZIO.succeed{
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    val cashFlowEntityRecords: Seq[CashFlowEntity] = {
      sql"""
          |select
          |id,
          |amount,
          |delta,
          |create_date,
          |modified_date
          |from cash_flow
          |""".stripMargin.map(rs => CashFlowEntity(rs)).list.apply()
    }

    cashFlowEntityRecords
  }
}

object CashFlowRepositoryImpl{
  private def apply = new CashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, CashFlowRepository] =
    ZLayer.succeed(apply)
}
