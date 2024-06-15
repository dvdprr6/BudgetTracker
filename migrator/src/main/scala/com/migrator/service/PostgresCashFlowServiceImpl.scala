package com.migrator.service

import com.migrator.models.{CashFlow, PostgresConnectionDto}
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresCashFlowRepository
import com.migrator.utils.{PostgresDbConnection, PostgresDbConnectionImpl}

trait PostgresCashFlowService{
  def insertCashFlowRecords(cashFlow: Seq[CashFlow])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresCashFlowServiceImpl(postgresCashFlowRepository: PostgresCashFlowRepository, postgresDbConnection: PostgresDbConnection) extends PostgresCashFlowService {


  override def insertCashFlowRecords(cashFlow: Seq[CashFlow])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] = {
    for{
      postgresSession <- postgresDbConnection.getPostgresSession
      _ <- postgresCashFlowRepository.truncate()(postgresSession)
      _ <- postgresCashFlowRepository.insert(cashFlow)(postgresSession)
    } yield ()
  }
}

object PostgresCashFlowServiceImpl{
  private type CashFlowServiceItem = PostgresDbConnection with PostgresCashFlowRepository

  private def apply(postgresCashFlowRepository: PostgresCashFlowRepository, postgresDbConnection: PostgresDbConnection) =
    new PostgresCashFlowServiceImpl(postgresCashFlowRepository, postgresDbConnection)

  lazy val live: ZLayer[CashFlowServiceItem, Throwable, PostgresCashFlowService] =
    ZLayer.fromFunction(apply _)
}