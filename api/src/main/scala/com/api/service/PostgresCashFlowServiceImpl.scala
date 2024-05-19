package com.api.repository

import com.api.models.CashFlowDto
import zio.{Task, ZLayer}

trait PostgresCashFlowService{
  def getCashFlowRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[CashFlowDto]]
}

class PostgresCashFlowServiceImpl(postgresRepository: PostgresRepository) extends PostgresCashFlowService {

  def getCashFlowRecords()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Seq[CashFlowDto]] =
    for{
      cashFlowRecords <- postgresRepository.getCashFlow()(postgresUrl, postgresUsername, postgresPassword)
    } yield cashFlowRecords
}

object PostgresCashFlowServiceImpl{
  private def apply(postgresRepository: PostgresRepository) =
    new PostgresCashFlowServiceImpl(postgresRepository)

  lazy val live: ZLayer[PostgresRepository, Throwable, PostgresCashFlowService] =
    ZLayer.fromFunction(apply _)
}