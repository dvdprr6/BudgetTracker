package com.commons.models

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class CashFlowDto(id: String,
                       amount: Double,
                       delta: Double,
                       createDate: String,
                       modifiedDate: String)

object CashFlowDto{
  implicit val encoder: JsonEncoder[CashFlowDto] = DeriveJsonEncoder.gen[CashFlowDto]
}
