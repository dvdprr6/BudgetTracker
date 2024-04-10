package com.migrator.utils

import zio.{ZIO, ZLayer}
import com.mongodb.client.{MongoClient, MongoClients}


class MongoDbConnection {
  private val URL = "";

  def getMongoClient(): ZIO[Any, Exception, MongoClient] =
    ZIO.succeed(MongoClients.create(""))
}

object MongoDbConnection{
  private def create() = new MongoDbConnection

  lazy val live: ZLayer[Any, Nothing, MongoDbConnection] =
    ZLayer.succeed(create)
}
