package com.migrator.test.mocks

import com.migrator.utils.PostgresDbConnection
import zio.{Task, URLayer, ZIO, ZLayer, mock}
import zio.mock.Mock

object PostgresDbConnectionMock extends Mock[PostgresDbConnection]{
  object Truncate extends Effect[String, Throwable, Unit]
  object Insert extends Effect[(String, Seq[Seq[(String, Any)]]), Throwable, Unit]

  val compose: URLayer[mock.Proxy, PostgresDbConnection] =
    ZLayer{
      for{
        proxy <- ZIO.service[mock.Proxy]
      } yield new PostgresDbConnection {
        override def insert(query: String, batchParams: Seq[Seq[(String, Any)]]): Task[Unit] =
          proxy(Insert, query, batchParams)

        override def truncate(query: String): Task[Unit] =
          proxy(Truncate, query)
      }
    }
}
