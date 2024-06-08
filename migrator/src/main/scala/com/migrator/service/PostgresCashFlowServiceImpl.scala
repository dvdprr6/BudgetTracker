package com.migrator.service

import com.migrator.models.{CashFlow, PostgresConnectionDto}
import zio.{Task, ZLayer}
import com.migrator.repository.PostgresCashFlowRepository

trait PostgresCashFlowService{
  def insertCashFlowRecords(cashFlow: Seq[CashFlow])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresCashFlowServiceImpl(postgresCashFlowRepository: PostgresCashFlowRepository) extends PostgresCashFlowService {


  override def insertCashFlowRecords(cashFlow: Seq[CashFlow])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] =
    for{
      _ <- postgresCashFlowRepository.truncate()
      _ <- postgresCashFlowRepository.insert(cashFlow)
    } yield ()
}

object PostgresCashFlowServiceImpl{
  private def apply(postgresCashFlowRepository: PostgresCashFlowRepository) =
    new PostgresCashFlowServiceImpl(postgresCashFlowRepository)

  lazy val live: ZLayer[PostgresCashFlowRepository, Throwable, PostgresCashFlowService] =
    ZLayer.fromFunction(apply _)
}