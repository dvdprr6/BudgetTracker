package com.api.models

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class ItemDto(id: String,
                   itemName: String,
                   amount: Double,
                   itemType: String,
                   categoryId: String,
                   createDate: String,
                   modifiedDate: String)

object ItemDto{
  implicit val encoder: JsonEncoder[ItemDto] = DeriveJsonEncoder.gen[ItemDto]
}
