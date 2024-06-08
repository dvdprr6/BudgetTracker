package com.migrator.repository

import com.migrator.models.Item
import com.migrator.utils.{PostgresConnection, Utils}
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresItemRepository{
  def insert(items: Seq[Item])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
  def truncate()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresItemRepositoryImpl extends PostgresItemRepository with PostgresConnection{

  override def insert(items: Seq[Item])(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] = ZIO.succeed {
    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    val batchParams: Seq[Seq[(String, Any)]] = items.map{record =>
      Seq(
        "id" -> record.id.toHexString,
        "item_name" -> record.itemName,
        "amount" -> record.amount,
        "item_type" -> record.itemType,
        "category_id" -> record.categoryId.toHexString,
        "create_date" -> Utils.dateToLocalDateTime(record.createDate),
        "modified_date" -> Utils.dateToLocalDateTime(record.modifiedDate)
      )
    }

    sql"""
          insert into item (id, item_name, amount, item_type, category_id, create_date, modified_date)
          values ({id}, {item_name}, {amount}, {item_type}, {category_id}, {create_date}, {modified_date})
       """
      .batchByName(batchParams: _*)
      .apply()
  }

  override def truncate()(postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] = ZIO.succeed {
    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    sql"""truncate table item""".update.apply()
  }
}

object PostgresItemRepositoryImpl{
  private def apply: PostgresItemRepository = new PostgresItemRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresItemRepository] =
    ZLayer.succeed(apply)
}
