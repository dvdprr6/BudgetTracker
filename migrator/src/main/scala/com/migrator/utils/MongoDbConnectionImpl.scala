package com.migrator.utils

import zio.{ZIO, ZLayer}
import com.mongodb.client.{MongoClient, MongoClients}

trait MongoDbConnection{
  def getMongoClient(): ZIO[Any, Exception, MongoClient]
}

class MongoDbConnectionImpl extends MongoDbConnection {
  private val URL = "";

  override def getMongoClient(): ZIO[Any, Exception, MongoClient] =
    ZIO.succeed(MongoClients.create(""))
}

object MongoDbConnectionImpl{
  private def create(): MongoDbConnection = new MongoDbConnectionImpl

  lazy val live: ZLayer[Any, Nothing, MongoDbConnection] =
    ZLayer.succeed(create)
}
