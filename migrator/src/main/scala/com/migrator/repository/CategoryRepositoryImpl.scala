package com.migrator.repository

import com.migrator.models.Category
import com.migrator.utils.{PostgresConnection, Utils}
import zio.{ZIO, ZLayer}
import zio.schema.DeriveSchema
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDateTime

trait PostgresCategoryRepository{
  def insert(categories: Seq[Category])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
}

class PostgresCategoryRepositoryImpl extends PostgresCategoryRepository with PostgresJdbcModule{

  import Entity._

  override def insert(categories: Seq[Category])(postgresUrl: TableName, postgresUsername: TableName, postgresPassword: TableName): ZIO[Any, Exception, Int] = {
    val categoryEntity = categories.map(record => toCategoryEntity(record))

    val statement = insertInto(categoryTable)(id, categoryName, createDate, modifiedDate).values(categoryEntity)

    execute(statement).provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

  private object Entity{
    case class CategoryEntity(id: String,
                              categoryName: String,
                              createDate: LocalDateTime,
                              modifiedDate: LocalDateTime)

    implicit val categorySchema = DeriveSchema.gen[CategoryEntity]

    val categoryTable = defineTable[CategoryEntity]("category")

    val (id, categoryName, createDate, modifiedDate) = categoryTable.columns

    def toCategoryEntity(category: Category): CategoryEntity = {
      val createDateLocalDateTime: LocalDateTime = Utils.dateToLocalDateTime(category.createDate)
      val modifiedDateLocalDateTime: LocalDateTime = Utils.dateToLocalDateTime(category.modifiedDate)

      CategoryEntity(
        category.id.toHexString,
        category.categoryName,
        createDateLocalDateTime,
        modifiedDateLocalDateTime
      )
    }
  }
}

object PostgresCategoryRepositoryImpl{
  private def apply: PostgresCategoryRepository = new PostgresCategoryRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, PostgresCategoryRepository] =
    ZLayer.succeed(apply)
}
