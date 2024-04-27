package com.commons.service

import com.commons.models.CashFlow
import com.commons.repository.PostgresCashFlowRepository
import zio.{Task, ZLayer}

trait PostgresCashFlowService{
  def insertCashFlowRecords(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresCashFlowServiceImpl(postgresCashFlowRepository: PostgresCashFlowRepository) extends PostgresCashFlowService {

  override def insertCashFlowRecords(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] =
    for{
      _ <- postgresCashFlowRepository.insert(cashFlow)(postgresUrl: String, postgresUsername: String, postgresPassword: String)
    } yield ()
}

object PostgresCashFlowServiceImpl{
  private def apply(postgresCashFlowRepository: PostgresCashFlowRepository) =
    new PostgresCashFlowServiceImpl(postgresCashFlowRepository)

  lazy val live: ZLayer[PostgresCashFlowRepository, Throwable, PostgresCashFlowService] =
    ZLayer.fromFunction(apply _)
}