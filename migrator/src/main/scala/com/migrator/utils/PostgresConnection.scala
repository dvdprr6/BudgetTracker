package com.migrator.utils

import scalikejdbc.{AutoSession, ConnectionPool, DBSession}

trait PostgresConnection {
  def getPostgresSession(postgresUrl: String , postgresUsername: String, postgresPassword: String): DBSession = {
    Class.forName("org.postgresql.Driver")
    ConnectionPool.singleton(postgresUrl, postgresUsername, postgresPassword)

    val session: DBSession = AutoSession

    session
  }
}
