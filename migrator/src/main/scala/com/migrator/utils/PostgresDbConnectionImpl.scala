package com.migrator.utils

import com.migrator.models.PostgresConnectionDto
import scalikejdbc.{AutoSession, ConnectionPool, DBSession}
import zio.{Task, ZIO, ZLayer}

trait PostgresDbConnection{
  def getPostgresSession(implicit postgresConnectionDto: PostgresConnectionDto): Task[DBSession]
}

class PostgresDbConnectionImpl extends PostgresDbConnection {

  override def getPostgresSession(implicit postgresConnectionDto: PostgresConnectionDto): Task[DBSession] = ZIO.succeed{
    val postgresUrl = postgresConnectionDto.postgresUrl
    val postgresUsername = postgresConnectionDto.postgresUsername
    val postgresPassword = postgresConnectionDto.postgresPassword

    Class.forName("org.postgresql.Driver")
    ConnectionPool.singleton(postgresUrl, postgresUsername, postgresPassword)

    val session: DBSession = AutoSession

    session
  }
}

object PostgresDbConnectionImpl{
  private def apply: PostgresDbConnection = new PostgresDbConnectionImpl

  lazy val live: ZLayer[Any, Nothing, PostgresDbConnection] =
    ZLayer.succeed(apply)
}