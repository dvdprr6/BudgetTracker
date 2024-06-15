package com.migrator.repository

import com.migrator.models.{Category, PostgresConnectionDto}
import com.migrator.utils.Utils
import scalikejdbc._
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresCategoryRepository{
  def insert(categories: Seq[Category])(dbSession: DBSession): Task[Unit]
  def truncate()(dbSession: DBSession): Task[Unit]
}

class PostgresCategoryRepositoryImpl extends PostgresCategoryRepository{

  override def insert(categories: Seq[Category])(dbSession: DBSession): Task[Unit] = ZIO.succeed {
    implicit val session = dbSession

    val batchParams: Seq[Seq[(String, Any)]] = categories.map { record =>
      Seq(
        "id" -> record.id.toHexString,
        "category_name" -> record.categoryName,
        "create_date" -> Utils.dateToLocalDateTime(record.createDate),
        "modified_date" -> Utils.dateToLocalDateTime(record.modifiedDate)
      )
    }

    sql"""
          insert into category (id, category_name, create_date, modified_date)
          values ({id}, {category_name}, {create_date}, {modified_date})
          """
      .batchByName(batchParams: _*)
      .apply
  }

  override def truncate()(dbSession: DBSession): Task[Unit] = ZIO.succeed {
    implicit val session = dbSession

    sql"""truncate table category""".update.apply()
  }
}

object PostgresCategoryRepositoryImpl{
  private def apply: PostgresCategoryRepository = new PostgresCategoryRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCategoryRepository] =
    ZLayer.succeed(apply)
}
