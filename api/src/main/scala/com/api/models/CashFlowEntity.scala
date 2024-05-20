package com.api.models

import scalikejdbc.{SQLSyntaxSupport, WrappedResultSet}

import java.time.LocalDateTime

case class CashFlowEntity(id: String, amount: Double, delta: Double, createDate: LocalDateTime, modifiedDate: LocalDateTime)

object CashFlowEntity extends SQLSyntaxSupport[CashFlowEntity]{
  override val tableName = "cash_flow"

  def apply(rs: WrappedResultSet): CashFlowEntity =
    CashFlowEntity(
      rs.string("id"),
      rs.double("amount"),
      rs.double("delta"),
      rs.localDateTime("create_date"),
      rs.localDateTime("modified_date")
    )
}
