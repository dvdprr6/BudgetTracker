package com.api.repository

import com.api.models.{CategoryGroupByWithTotalsEntity, PostgresConnectionDto}
import com.api.utils.PostgresConnection
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef
import zio._

trait CategoryRepository{
  def getWithGroupByTotals()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CategoryGroupByWithTotalsEntity]]
}

class CategoryRepositoryImpl extends CategoryRepository with PostgresConnection{

  override def getWithGroupByTotals()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CategoryGroupByWithTotalsEntity]] = ZIO.succeed{
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    val categoryGroupByWithTotalsEntityRecords: Seq[CategoryGroupByWithTotalsEntity] =
      sql"""
           |select
           |c.id,
           |c.category_name,
           |sum(i.amount) as total,
           |c.create_date,
           |c.modified_date
           |from category c
           |inner join item i
           |on c.id = i.category_id
           |group by
           |c.id,
           |c.category_name,
           |c.create_date,
           |c.modified_date
           |""".stripMargin.map(rs => CategoryGroupByWithTotalsEntity(rs)).list.apply()

    categoryGroupByWithTotalsEntityRecords

  }
}

object CategoryRepositoryImpl{
  private def apply = new CategoryRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, CategoryRepository] =
    ZLayer.succeed(apply)
}
