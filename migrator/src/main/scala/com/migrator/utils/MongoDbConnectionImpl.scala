package com.migrator.utils

import com.migrator.codec.{CashFlowCodec, CategoryCodec, ItemCodec}
import com.mongodb.{ConnectionString, MongoClientSettings}
import zio.{ZIO, ZLayer}
import com.mongodb.client.{MongoClient, MongoClients}
import org.bson.codecs.configuration.CodecRegistries

trait MongoDbConnection{
  def getMongoClient(mongoDbUrl: String): ZIO[Any, Exception, MongoClient]
}

class MongoDbConnectionImpl extends MongoDbConnection {

  override def getMongoClient(mongoDbUrl: String): ZIO[Any, Exception, MongoClient] = ZIO.succeed{
    val itemCodec = new ItemCodec
    val categoryCodec = new CategoryCodec
    val cashFlowCodec = new CashFlowCodec

    val codecRegistries = CodecRegistries.fromCodecs(itemCodec, categoryCodec, cashFlowCodec)
    val settings: MongoClientSettings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString(mongoDbUrl))
      .codecRegistry(codecRegistries)
      .build()

    MongoClients.create(settings)
  }
}

object MongoDbConnectionImpl{
  private def apply: MongoDbConnection = new MongoDbConnectionImpl

  lazy val live: ZLayer[Any, Nothing, MongoDbConnection] =
    ZLayer.succeed(apply)
}
