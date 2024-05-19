package com.api.models

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class CategoryDto(id: String,
                       categoryName: String,
                       createDate: String,
                       modifiedDate: String)


object CategoryDto{
  implicit val encoder: JsonEncoder[CategoryDto] = DeriveJsonEncoder.gen[CategoryDto]
}