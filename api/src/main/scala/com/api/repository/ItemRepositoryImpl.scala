package com.api.repository

import com.api.models.ItemEntity
import com.api.utils.PostgresDbConnection
import zio._

trait ItemRepository{
  def getItems(): Task[Seq[ItemEntity]]
  def getItemsByCategoryId(categoryId: String): Task[Seq[ItemEntity]]
}

class ItemRepositoryImpl(postgresDbConnection: PostgresDbConnection) extends ItemRepository{

  override def getItems(): Task[Seq[ItemEntity]] = {
    val query =
      """
        |SELECT
        |id,
        |item_name,
        |amount,
        |item_type,
        |category_id,
        |create_date,
        |modified_date
        |FROM item
        |""".stripMargin

    for{
      itemRecords <- postgresDbConnection.get[ItemEntity](query, ItemEntity)
    } yield itemRecords
  }

  override def getItemsByCategoryId(categoryId: String): Task[Seq[ItemEntity]] = {
    val query =
      s"""
        |SELECT
        |id,
        |item_name,
        |amount,
        |item_type,
        |category_id,
        |create_date,
        |modified_date
        |FROM item WHERE category_id = '$categoryId'
        |""".stripMargin

    for{
      itemRecords <- postgresDbConnection.get[ItemEntity](query, ItemEntity)
    } yield itemRecords
  }
}

object ItemRepositoryImpl{
  private def apply(postgresDbConnection: PostgresDbConnection) =
    new ItemRepositoryImpl(postgresDbConnection)

  lazy val live: ZLayer[PostgresDbConnection, Throwable, ItemRepository] =
    ZLayer.fromFunction(apply _)
}