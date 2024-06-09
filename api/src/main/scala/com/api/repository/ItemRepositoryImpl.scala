package com.api.repository

import com.api.models.{ItemEntity, PostgresConnectionDto}
import com.api.utils.PostgresConnection
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef
import zio.{Task, ZIO, ZLayer}

trait ItemRepository{
  def get()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[ItemEntity]]
  def getItemsByCategoryId(categoryId: String)(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[ItemEntity]]
}

class ItemRepositoryImpl extends ItemRepository with PostgresConnection{

  override def get()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[ItemEntity]] = ZIO.succeed{
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    val itemRecords: Seq[ItemEntity] =
      sql"""
           |select
           |i.id,
           |i.item_name,
           |i.amount,
           |i.item_type,
           |i.category_id,
           |i.create_date,
           |i.modified_date
           |from item i
           |""".stripMargin.map(rs => ItemEntity(rs)).list.apply()

    itemRecords
  }

  override def getItemsByCategoryId(categoryId: String)(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[ItemEntity]] = ZIO.succeed {
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    val itemsByCategoryIdRecords: Seq[ItemEntity] =
      sql"""
           |select
           |i.id,
           |i.item_name,
           |i.amount,
           |i.item_type,
           |i.category_id,
           |i.create_date,
           |i.modified_date
           |from item i where i.category_id = $categoryId
           |""".stripMargin.map(rs => ItemEntity(rs)).list.apply()


    itemsByCategoryIdRecords
  }

}

object ItemRepositoryImpl{
  private def apply = new ItemRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, ItemRepository] =
    ZLayer.succeed(apply)
}