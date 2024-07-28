package com.migrator.repository

import com.migrator.models.Item
import com.migrator.utils.{PostgresDbConnection, Utils}
import zio.{Task, ZLayer}

trait PostgresItemRepository{
  def insertItem(items: Seq[Item]): Task[Unit]
  def truncateItem(): Task[Unit]
}

class PostgresItemRepositoryImpl(postgresDbConnection: PostgresDbConnection) extends PostgresItemRepository{

  override def insertItem(items: Seq[Item]): Task[Unit] = {
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

    val insertQuery =
      """
        | INSERT INTO item (id, item_name, amount, item_type, category_id, create_date, modified_date)
        | VALUES ({id}, {item_name}, {amount}, {item_type}, {category_id}, {create_date}, {modified_date})""".stripMargin

    for {
      _ <- postgresDbConnection.insert(insertQuery, batchParams)
    } yield ()

  }

  override def truncateItem(): Task[Unit] =
    for {
      _ <- postgresDbConnection.truncate("item")
    } yield ()
}

object PostgresItemRepositoryImpl{
  private def apply(postgresDbConnection: PostgresDbConnection): PostgresItemRepository =
    new PostgresItemRepositoryImpl(postgresDbConnection)

  lazy val live: ZLayer[PostgresDbConnection, Throwable, PostgresItemRepository] =
    ZLayer.fromFunction(apply _)
}
