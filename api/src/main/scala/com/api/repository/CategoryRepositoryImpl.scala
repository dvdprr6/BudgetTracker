package com.api.repository

import com.api.models.CategoryGroupByWithTotalsEntity
import com.api.utils.PostgresDbConnection
import zio._

trait CategoryRepository{
  def getWithGroupByTotals(): Task[Seq[CategoryGroupByWithTotalsEntity]]
}

class CategoryRepositoryImpl(postgresDbConnection: PostgresDbConnection) extends CategoryRepository{

  override def getWithGroupByTotals(): Task[Seq[CategoryGroupByWithTotalsEntity]] = {
    val query =
      """
        |SELECT c.id, c.category_name, sum(i.amount) as total, c.create_date, c.modified_date
        |FROM category c
        |INNER JOIN item i on c.id = i.category_id
        |GROUP BY
        |c.id, c.category_name, c.create_date, c.modified_date
        |""".stripMargin

    for{
      categoryGroupByWithTotalsEntity <- postgresDbConnection.get[CategoryGroupByWithTotalsEntity](query, CategoryGroupByWithTotalsEntity)
    } yield categoryGroupByWithTotalsEntity
  }
}

object CategoryRepositoryImpl{
  private def apply(postgresDbConnection: PostgresDbConnection) =
    new CategoryRepositoryImpl(postgresDbConnection: PostgresDbConnection)

  lazy val live: ZLayer[PostgresDbConnection, Throwable, CategoryRepository] =
    ZLayer.fromFunction(apply _)
}
