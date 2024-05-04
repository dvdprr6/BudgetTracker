package com.commons.service

import com.commons.models.{CashFlow, CashFlowDto}
import com.commons.repository.PostgresCashFlowRepository
import com.commons.utils.Utils
import zio.{Task, ZLayer}

trait PostgresCashFlowService{
  def getCashFlowRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[CashFlowDto]]

  def insertCashFlowRecords(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresCashFlowServiceImpl(postgresCashFlowRepository: PostgresCashFlowRepository) extends PostgresCashFlowService {

  def getCashFlowRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[CashFlowDto]] =
    for{
      cashFlowRecords <- postgresCashFlowRepository.get()(postgresUrl, postgresUsername, postgresPassword)
      cashFlowDtoRecords = cashFlowRecords.map(record => Utils.cashFlowToCashFlowDto(record))
    } yield cashFlowDtoRecords


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