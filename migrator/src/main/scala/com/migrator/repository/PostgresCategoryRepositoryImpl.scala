package com.migrator.repository

import com.migrator.models.Category
import com.migrator.utils.{PostgresDbConnection, Utils}
import zio.{Task, ZLayer}


trait PostgresCategoryRepository{
  def insertCategory(categories: Seq[Category]): Task[Unit]
  def truncateCategory(): Task[Unit]
}

class PostgresCategoryRepositoryImpl(postgresDbConnection: PostgresDbConnection) extends PostgresCategoryRepository{

  override def insertCategory(categories: Seq[Category]): Task[Unit] = {
    val batchParams: Seq[Seq[(String, Any)]] = categories.map { record =>
      Seq(
        "id" -> record.id.toHexString,
        "category_name" -> record.categoryName,
        "create_date" -> Utils.dateToLocalDateTime(record.createDate),
        "modified_date" -> Utils.dateToLocalDateTime(record.modifiedDate)
      )
    }

    val insertQuery =
      """
        | INSERT INTO category (id, category_name, create_date, modified_date)
        | values ({id}, {category_name}, {create_date}, {modified_date})""".stripMargin

    for{
      _ <- postgresDbConnection.insert(insertQuery, batchParams)
    } yield ()
  }

  override def truncateCategory(): Task[Unit] =
    for{
      _ <- postgresDbConnection.truncate("category")
    } yield ()
}

object PostgresCategoryRepositoryImpl{
  private def apply(postgresDbConnection: PostgresDbConnection): PostgresCategoryRepository =
    new PostgresCategoryRepositoryImpl(postgresDbConnection)

  lazy val live: ZLayer[PostgresDbConnection, Throwable, PostgresCategoryRepository] =
    ZLayer.fromFunction(apply _)
}
