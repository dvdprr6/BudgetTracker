package com.api.repository

import com.api.models.{CashFlowDto, CashFlowEntity, PostgresConnectionDto}
import com.api.utils.Utils
import zio.{Task, ZLayer}

trait CashFlowService{
  def getCashFlowRecords()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CashFlowDto]]
}

class CashFlowServiceImpl(cashFlowRepository: CashFlowRepository) extends CashFlowService {

  override def getCashFlowRecords()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CashFlowDto]] = {
    val cashFlowRecordsDto = for{
      cashFlowEntity <- cashFlowRepository.get()
      cashFlowDto = cashFlowEntity.map(record => toCashFlowDto(record))
    } yield cashFlowDto

    cashFlowRecordsDto
  }

  private def toCashFlowDto(cashFlowEntity: CashFlowEntity): CashFlowDto = {
    val id = cashFlowEntity.id
    val amount = cashFlowEntity.amount
    val delta = cashFlowEntity.delta
    val createDate = Utils.localDateTimeToString(cashFlowEntity.createDate)
    val modifiedDate = Utils.localDateTimeToString(cashFlowEntity.modifiedDate)

    CashFlowDto(id, amount, delta, createDate, modifiedDate)

  }

}

object CashFlowServiceImpl{
  private def apply(cashFlowRepository: CashFlowRepository): CashFlowService =
    new CashFlowServiceImpl(cashFlowRepository)

  lazy val live: ZLayer[CashFlowRepository, Throwable, CashFlowService] =
    ZLayer.fromFunction(apply _)
}