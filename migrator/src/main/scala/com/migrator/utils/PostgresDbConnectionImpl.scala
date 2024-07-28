package com.migrator.utils

import com.migrator.models.PostgresConnectionDto
import scalikejdbc.interpolation.SQLSyntax
import scalikejdbc.{AutoSession, ConnectionPool, DBSession, scalikejdbcSQLInterpolationImplicitDef}
import zio.{Task, ZIO, ZLayer}

import scala.collection.IndexedSeq.iterableFactory

trait PostgresDbConnection{
  def insert(query: String, batchParams: Seq[Seq[(String, Any)]]): Task[Unit]
  def truncate(tableName: String): Task[Unit]
}

class PostgresDbConnectionImpl(postgresConnectionDto: PostgresConnectionDto) extends PostgresDbConnection {
  private val POSTGRES_DRIVER = "org.postgresql.Driver"

  override def insert(query: String, batchParams: Seq[Seq[(String, Any)]]): Task[Unit] =
    ZIO.succeed {
      implicit val session: DBSession = postgresSession(postgresConnectionDto)
      val insertQuery = SQLSyntax.createUnsafely(query)
      sql"$insertQuery".batchByName(batchParams: _*).apply()
      session.close()
    }

  override def truncate(tableName: String): Task[Unit] =
    ZIO.succeed{
      implicit val session: DBSession = postgresSession(postgresConnectionDto)
      val truncateQuery = SQLSyntax.createUnsafely(s"TRUNCATE TABLE $tableName")
      sql"$truncateQuery".update.apply()
      session.close()
    }

  private def postgresSession(postgresConnectionDto: PostgresConnectionDto): DBSession = {
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    Class.forName(POSTGRES_DRIVER)
    ConnectionPool.singleton(postgresUrl, postgresUsername, postgresPassword)

    val session: DBSession = AutoSession

    session
  }
}

object PostgresDbConnectionImpl{
  private def apply(postgresConnectionDto: PostgresConnectionDto): PostgresDbConnection =
    new PostgresDbConnectionImpl(postgresConnectionDto)

  lazy val live: ZLayer[PostgresConnectionDto, Nothing, PostgresDbConnection] =
    ZLayer.fromFunction(apply _)
}