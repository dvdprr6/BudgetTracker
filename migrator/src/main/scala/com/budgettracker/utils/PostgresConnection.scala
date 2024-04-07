package com.budgettracker.utils

import zio.ZLayer
import zio.sql.ConnectionPoolConfig

import java.util.Properties

object PostgresConnection {
  private val USER = "david"
  private val PASSWORD = "password"
  private val URL = "jdbc:postgresql://localhost:5432/budgettracker"

  lazy val live: ZLayer[Any, Nothing, ConnectionPoolConfig] =
    ZLayer.succeed{
      val properties = new Properties
      properties.setProperty("user", USER)
      properties.setProperty("password", PASSWORD)

      ConnectionPoolConfig(
        url = URL,
        properties = properties,
        autoCommit = true
      )
    }
}
