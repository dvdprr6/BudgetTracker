package com.api.utils

import com.api.models.{Entity, PostgresConnectionDto}
import scalikejdbc.interpolation.SQLSyntax
import scalikejdbc.{AutoSession, ConnectionPool, DBSession, scalikejdbcSQLInterpolationImplicitDef}
import zio.{Task, ZIO, ZLayer}

trait PostgresDbConnection{
  def get[T](query: String, clazz: Entity[T]): Task[Seq[T]]
}

class PostgresConnectionDbImpl(postgresConnectionDto: PostgresConnectionDto) extends PostgresDbConnection {
  private val POSTGRES_DRIVER = "org.postgresql.Driver"

  override def get[T](query: String, entity: Entity[T]): Task[Seq[T]] =
    ZIO.succeed{
      implicit val session: DBSession = postgresSession(postgresConnectionDto)
      val selectQuery = SQLSyntax.createUnsafely(query)
      val queryResults = sql"$selectQuery".map(rs => entity(rs)).list.apply
      session.close()

      queryResults
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
    new PostgresConnectionDbImpl(postgresConnectionDto)

  lazy val live: ZLayer[PostgresConnectionDto, Nothing, PostgresDbConnection] =
    ZLayer.fromFunction(apply _)
}
