package com.commons.repository

import com.commons.models.Item
import com.commons.utils.{PostgresConnection, Utils}
import org.bson.types.ObjectId
import zio.{ZIO, ZLayer}
import zio.schema.DeriveSchema
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDateTime

trait PostgresItemRepository{
  def get()(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Seq[Item]]

  def insert(items: Seq[Item])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
}

class PostgresItemRepositoryImpl extends PostgresItemRepository with PostgresJdbcModule{

  import Entity._

  override def get()(postgresUrl: TableName, postgresUsername: TableName, postgresPassword: TableName): ZIO[Any, Exception, Seq[Item]] = {
    val statement = select(id, itemName, amount, itemType, categoryId, createDate, modifiedDate).from(itemTable)

    val executeStatement = for {
      itemRecords <- execute(statement).map(ItemEntity tupled _).runCollect
      records = itemRecords.map(record => toItem(record))
    } yield records

    executeStatement.provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

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

    def toItem(itemEntity: ItemEntity): Item = {
      val createDate = Utils.localDateTimeToDate(itemEntity.createDate)
      val modifiedDate = Utils.localDateTimeToDate(itemEntity.modifiedDate)

      val objectId = new ObjectId(itemEntity.id)
      val categoryObjectId = new ObjectId(itemEntity.categoryId)

      Item(objectId, itemEntity.itemName, itemEntity.amount, itemEntity.itemType, categoryObjectId, createDate, modifiedDate)
    }
  }
}

object PostgresItemRepositoryImpl{
  private def apply: PostgresItemRepository = new PostgresItemRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresItemRepository] =
    ZLayer.succeed(apply)
}
