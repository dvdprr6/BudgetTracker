package com.commons.utils

import zio.ZLayer
import zio.sql.ConnectionPoolConfig

import java.util.Properties

object PostgresConnection {
  lazy val live = (postgresUrl: String, postgresUsername: String, postgresPassword: String) =>
    ZLayer.succeed{
      val properties = new Properties
      properties.setProperty("user", postgresUsername)
      properties.setProperty("password", postgresPassword)

      ConnectionPoolConfig(
        url = postgresUrl,
        properties = properties,
        autoCommit = true
      )
    }
}
