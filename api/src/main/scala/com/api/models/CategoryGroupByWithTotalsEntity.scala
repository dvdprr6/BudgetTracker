package com.api.models

import scalikejdbc.{SQLSyntaxSupport, WrappedResultSet}

import java.time.LocalDateTime

case class CategoryGroupByWithTotalsEntity(
                                            id: String,
                                            categoryName: String,
                                            total: Double,
                                            createDate: LocalDateTime,
                                            modifiedDate: LocalDateTime
                                           )

object CategoryGroupByWithTotalsEntity extends SQLSyntaxSupport[CategoryGroupByWithTotalsEntity]{
  override val tableName = "category"

  def apply(rs: WrappedResultSet): CategoryGroupByWithTotalsEntity =
    CategoryGroupByWithTotalsEntity(
      rs.string("id"),
      rs.string("category_name"),
      rs.double("total"),
      rs.localDateTime("create_date"),
      rs.localDateTime("modified_date")
    )
}
