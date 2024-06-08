package com.migrator.repository

import com.migrator.models.{Category, PostgresConnectionDto}
import com.migrator.utils.{PostgresConnection, Utils}
import scalikejdbc._
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresCategoryRepository{
  def insert(categories: Seq[Category])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
  def truncate()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit]
}

class PostgresCategoryRepositoryImpl extends PostgresCategoryRepository with PostgresConnection{

  override def insert(categories: Seq[Category])(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] = ZIO.succeed {
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

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

  override def truncate()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Unit] = ZIO.succeed {
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    implicit val session = getPostgresSession(postgresUrl, postgresUsername, postgresPassword)

    sql"""truncate table category""".update.apply()

  }
}

object PostgresCategoryRepositoryImpl{
  private def apply: PostgresCategoryRepository = new PostgresCategoryRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCategoryRepository] =
    ZLayer.succeed(apply)
}
