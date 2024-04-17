package com.migrator.repository

import com.migrator.models.Item
import com.migrator.utils.{PostgresConnection, Utils}
import zio.{ZIO, ZLayer}
import zio.schema.DeriveSchema
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDateTime

trait PostgresItemRepository{
  def insert(items: Seq[Item])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
}

class PostgresItemRepositoryImpl extends PostgresItemRepository with PostgresJdbcModule{

  import Entity._

  override def insert(items: Seq[Item])(postgresUrl: TableName, postgresUsername: TableName, postgresPassword: TableName): ZIO[Any, Exception, Int] = {
    val itemEntity = items.map(record => toItemEntity(record))

    val statement = insertInto(itemTable)(id, itemName, amount, itemType, categoryId, createDate, modifiedDate).values(itemEntity)

    execute(statement).provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

  private object Entity{
    case class ItemEntity(id: String,
                          itemName: String,
                          amount: Double,
                          itemType: String,
                          categoryId: String,
                          createDate: LocalDateTime,
                          modifiedDate: LocalDateTime)

    implicit val itemSchema = DeriveSchema.gen[ItemEntity]

    val itemTable = defineTable[ItemEntity]("item")

    val (id, itemName, amount, itemType, categoryId, createDate, modifiedDate) = itemTable.columns

    def toItemEntity(item: Item): ItemEntity = {
      val createDateLocalDateTime: LocalDateTime = Utils.dateToLocalDateTime(item.createDate)
      val modifiedDateLocalDateTime: LocalDateTime = Utils.dateToLocalDateTime(item.modifiedDate)

      ItemEntity(
        item.id.toHexString,
        item.itemName,
        item.amount,
        item.itemType,
        item.categoryId.toHexString,
        createDateLocalDateTime,
        modifiedDateLocalDateTime
      )
    }
  }
}

object PostgresItemRepositoryImpl{
  private def apply: PostgresItemRepository = new PostgresItemRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresItemRepository] =
    ZLayer.succeed(apply)
}
