package com.migrator.utils

import com.migrator.codec.{CashFlowCodec, CategoryCodec, ItemCodec}
import com.migrator.utils.Constants.MONGODB_DATABASE
import com.mongodb.{ConnectionString, MongoClientSettings}
import zio.{ZIO, ZLayer}
import com.mongodb.client.MongoClients
import org.bson.codecs.configuration.CodecRegistries

trait MongoDbConnection{
  def getMongoRecords[T: zio.Tag](collectionName: String, clazz: Class[T]): ZIO[Any, Exception, Seq[T]]
}

class MongoDbConnectionImpl(mongoDbUrl: String) extends MongoDbConnection {

  override def getMongoRecords[T: zio.Tag](collectionName: String, clazz: Class[T]): ZIO[Any, Exception, Seq[T]] =
    ZIO.succeed{
      val itemCodec = new ItemCodec
      val categoryCodec = new CategoryCodec
      val cashFlowCodec = new CashFlowCodec

      val codecRegistries = CodecRegistries.fromCodecs(itemCodec, categoryCodec, cashFlowCodec)
      val settings: MongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(mongoDbUrl))
        .codecRegistry(codecRegistries)
        .build()

      val collection = MongoClients.create(settings)
        .getDatabase(MONGODB_DATABASE)
        .getCollection(collectionName, clazz)

      var records: Seq[T] = Seq()

      collection.find().forEach { item =>
        records = records :+ item
      }

      records

    }
}

object MongoDbConnectionImpl{
  private def apply(mongoDbUrl: String): MongoDbConnection =
    new MongoDbConnectionImpl(mongoDbUrl)

  lazy val live: ZLayer[String, Nothing, MongoDbConnection] =
    ZLayer.fromFunction(apply _)
}


