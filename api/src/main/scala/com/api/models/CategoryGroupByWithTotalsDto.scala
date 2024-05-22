package com.api.models

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class CategoryGroupByWithTotalsDto(
                                         id: String,
                                         categoryName: String,
                                         total: Double,
                                         createDate: String,
                                         modifiedDate: String
                                       )

object CategoryGroupByWithTotalsDto{
  implicit val encoder: JsonEncoder[CategoryGroupByWithTotalsDto] = DeriveJsonEncoder.gen[CategoryGroupByWithTotalsDto]
}