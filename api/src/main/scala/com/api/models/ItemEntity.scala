package com.api.models

import scalikejdbc.{SQLSyntaxSupport, WrappedResultSet}

import java.time.LocalDateTime

case class ItemEntity(id: String,
                      itemName: String,
                      amount: Double,
                      itemType: String,
                      categoryId: String,
                      createDate: LocalDateTime,
                      modifiedDate: LocalDateTime
                     )

object ItemEntity extends SQLSyntaxSupport[ItemEntity]{
  override val tableName = "item"

  def apply(rs: WrappedResultSet): ItemEntity =
    ItemEntity(
      rs.string("id"),
      rs.string("item_name"),
      rs.double("amount"),
      rs.string("item_type"),
      rs.string("category_id"),
      rs.localDateTime("create_date"),
      rs.localDateTime("modified_date")
    )
}
