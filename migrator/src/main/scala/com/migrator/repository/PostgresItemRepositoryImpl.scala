package com.migrator.repository

import com.migrator.models.{Item, PostgresConnectionDto}
import com.migrator.utils.Utils
import scalikejdbc.{DBSession, scalikejdbcSQLInterpolationImplicitDef}
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresItemRepository{
  def insert(items: Seq[Item])(dbSession: DBSession): Task[Unit]
  def truncate()(dbSession: DBSession): Task[Unit]
}

class PostgresItemRepositoryImpl extends PostgresItemRepository{

  override def insert(items: Seq[Item])(dbSession: DBSession): Task[Unit] = ZIO.succeed {

    implicit val session = dbSession

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

  override def truncate()(dbSession: DBSession): Task[Unit] = ZIO.succeed {
    implicit val session = dbSession

    sql"""truncate table item""".update.apply()
  }
}

object PostgresItemRepositoryImpl{
  private def apply: PostgresItemRepository = new PostgresItemRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresItemRepository] =
    ZLayer.succeed(apply)
}
