package com.migrator.test.mocks

import com.migrator.utils.MongoDbConnection
import zio.mock.Mock
import zio.{URLayer, ZIO, ZLayer, mock}

object MongoDbConnectionMock extends Mock[MongoDbConnection] {
  object GetMongoRecords extends Poly.Effect.InputErrorOutput

  val compose: URLayer[mock.Proxy, MongoDbConnection] =
    ZLayer {
      for {
        proxy <- ZIO.service[mock.Proxy]
      } yield new MongoDbConnection {
        override def getMongoRecords[T: zio.Tag](mongoDbUrl: String, collectionName: String, clazz: Class[T]): ZIO[Any, Exception, Seq[T]] =
          proxy(GetMongoRecords.of[(String, String, Class[T]), Exception, Seq[T]], mongoDbUrl, collectionName, clazz)
      }
    }
}
